package net.arin.tp.processor.template;

import com.google.common.base.Function;
import net.arin.tp.utils.Constants;
import net.arin.tp.processor.Resolver;
import net.arin.tp.processor.exception.InvalidTemplateActionException;
import net.arin.tp.processor.exception.MultipleKeyException;
import net.arin.tp.processor.exception.TemplateFieldLimitExceededException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains shared logic for all email templates.
 */
public abstract class TemplateImpl implements Serializable, Template
{
    protected static final Logger log = LoggerFactory.getLogger( TemplateImpl.class );

    /**
     * This label denotes the beginning of the template and is followed by a version number.
     */
    private static final String TEMPLATE_LABEL = "Template:";

    /**
     * This label denotes the end of the template. It comes before the instructions.
     */
    private static final String TEMPLATE_FOOTER = "END OF TEMPLATE";

    /**
     * This pattern is used throughout the template processor as a quick check to determine whether the content we're
     * working with is actually a template.
     */
    public static final Pattern TEMPLATE_CHECK_PATTERN = Pattern.compile( "^\\s*(" + TEMPLATE_LABEL + ".*?" + TEMPLATE_FOOTER + ")", Pattern.DOTALL | Pattern.MULTILINE );

    public static final Pattern API_KEY_PATTERN = Pattern.compile( Constants.API_KEY, Pattern.DOTALL );

    /**
     * A regex pattern for matching the labels of template key-value pairs. It should match a pattern similar to
     * "01. Label:" where the digit and period are optional.
     */
    private final Pattern TEMPLATE_KEY_PATTERN = Pattern.compile( String.format( "^\\s*(?:\\d+.)?\\s*(%s)", getFieldWithWhitespaceMatchString() ), Pattern.MULTILINE | Pattern.CASE_INSENSITIVE );

    /**
     * A regex pattern for matching the comments of the template. Comments in templates match a pattern of
     * "** YOUR COMMENT HERE".
     */
    private static final Pattern TEMPLATE_COMMENT_PATTERN = Pattern.compile( "^\\s*\\*{2}(.*)$", Pattern.MULTILINE );

    /**
     * A regex pattern for matching the footer "END OF TEMPLATE" text and any text beyond it.
     */
    private static final Pattern TEMPLATE_FOOTER_PATTERN = Pattern.compile( "\\n*" + TEMPLATE_FOOTER + ".*", Pattern.DOTALL );

    /**
     * A regex pattern for matching the template header. This is followed by a name and version number.
     */
    private static final String TEMPLATE_NAME_REGEX = "^\\s*" + TEMPLATE_LABEL + "[\\s\\t]*([\\w\\-\\.]+).*?\\n.*";
    private static final Pattern TEMPLATE_NAME_PATTERN = Pattern.compile( TEMPLATE_NAME_REGEX, Pattern.DOTALL );

    static final String API_KEY_LABEL = "API Key:";

    static final String YES = "YES";
    static final String NO = "NO";

    private String apiKey;
    private List<Attachment> attachments = new ArrayList<>();

    public enum Action
    {
        CREATE( "N" ),
        MODIFY( "M" ),
        REMOVE( "R" );

        public String templateAction;

        Action( String templateAction )
        {
            this.templateAction = templateAction;
        }

        public static Action fromTemplate( String value )
        {
            for ( Action action : Action.values() )
            {
                if ( action.templateAction.equals( value.toUpperCase() ) )
                {
                    return action;
                }
            }

            throw new InvalidTemplateActionException( value.toUpperCase() );
        }
    }

    protected abstract List<String> getFieldKeyValues();

    /*
     * Given the Field Key Values specified by the concrete template via the getFieldKeyValues() implementation, create
     * a list of regular expressions that will match said Field Key Values under various whitespace permutations.
     */
    protected final List<String> getFieldPatterns()
    {
        List<String> patterns = new ArrayList<>();

        String pattern;

        for ( String key : getFieldKeyValues() )
        {
            pattern = key.trim();

            // Replace any , : or / with a whitespace wrapped version of itself.
            pattern = pattern.replaceAll( "\\s*([,:/])\\s*", "\\\\s*$1\\\\s*" );

            // Any field that contains parens should allow that information to be optional; so we'll replace fields
            // like: Registration Action (N, M or R) with Registration Action (\\(.*\\))?\\s*:
            pattern = pattern.replaceAll( "\\s*\\(.*\\)\\s*", "\\\\s*(\\\\(.*\\\\))?\\\\s*" );

            // Now replace any whitespace we find with a whitespace regex.
            pattern = pattern.replaceAll( "\\s+", "\\\\s+" );

            // Optimization.
            pattern = pattern.replaceAll( "(\\\\s\\*){2,}", "\\\\s*" ); // Get rid of consecutive \s* expressions.

            patterns.add( pattern );
        }

        return patterns;
    }

    protected final String getFieldWithWhitespaceMatchString()
    {
        return StringUtils.join( getFieldPatterns(), "|" );
    }

    protected abstract boolean isMultiLineField( String fieldName );

    protected abstract void mapKeyValuePair( String key, String value );

    static String normalizeKey( String key )
    {
        // Remove anything within parentheses.
        key = key.replaceAll( "\\(.*\\)", "" );

        // Remove all whitespace.
        key = key.replaceAll( "\\s+", "" );

        // Lowercase.
        key = key.toLowerCase();

        // Now we're ready to compare the two strings coming in.
        return key;
    }

    static boolean normalizedKeyCompare( String desired, String actual )
    {
        return normalizeKey( desired ).equals( normalizeKey( actual ) );
    }

    public void parseTemplate( String template )
    {
        // Some place to count field occurrences.
        Map<String, Integer> occurrences = new HashMap<>();

        // First remove all the comments so we know we're only dealing with user-entered data.
        Matcher matcher = TEMPLATE_COMMENT_PATTERN.matcher( template );
        int index = 0;

        String preprocessed = matcher.replaceAll( "" );

        // Begin processing the template.
        matcher = TEMPLATE_KEY_PATTERN.matcher( preprocessed );
        while ( matcher.find( index ) )
        {
            // Find the end of this current match.
            index = matcher.end();

            // Mark the group 1 as the key.
            String key = matcher.group( 1 );
            String normalizedKey = normalizeKey( key );
            String value;

            if ( matcher.find( index ) )
            {
                // If we successfully found another match, we must still have fields to process so we can utilize the
                // start index of the next match and the end index of the previous to get the full block of text.

                int lookahead = matcher.start();

                value = preprocessed.substring( index, lookahead );
            }
            else
            {
                // Maybe the end of document?

                value = preprocessed.substring( index );

                Matcher endOfDocument = TEMPLATE_FOOTER_PATTERN.matcher( value );

                if ( endOfDocument.find() )
                {
                    // If we find the end of the document then replace everything past the "END OF TEMPLATE" text with
                    // an empty string. There's some footer text that follows that will need to be removed too.

                    value = endOfDocument.replaceAll( "" );
                }
                else
                {
                    // Uh-oh, we hit the end of the string but didn't find the END OF TEMPLATE string. Something went
                    // wrong. We can still try to process the template but it probably will fail.

                    value = null;

                    log.debug( "Could not parse value for field=[" + key + "] because of no future matches and no end of document" );
                }
            }

            // If value is null, bail out.
            if ( value == null )
            {
                continue;
            }

            value = value.trim();

            // Skip blank lines.
            if ( value.isEmpty() )
            {
                continue;
            }

            log.info( "field=[" + key + "] value=[" + value + "]" );
            if ( normalizedKeyCompare( API_KEY_LABEL, key ) )
            {
                if ( this.apiKey == null )
                {
                    this.apiKey = value;
                }
                else if ( !this.apiKey.equals( value ) )
                {
                    throw new MultipleKeyException();
                }
            }
            else
            {
                Integer fieldCount = occurrences.get( normalizedKey ) == null ? 0 : occurrences.get( normalizedKey );
                fieldCount++;

                // If this is not a multi-line field it:
                // i)  should be the first time we've seen it and
                // ii) should not span multiple lines
                if ( !isMultiLineField( normalizedKey ) && ( fieldCount > 1 || value.contains( "\n" ) ) )
                {
                    throw new TemplateFieldLimitExceededException( key );
                }

                occurrences.put( normalizedKey, fieldCount );

                mapKeyValuePair( normalizedKey, value );
            }
        }
    }

    /**
     * @return A template implementation or an UnassignableTemplate if no template could be resolved.
     */
    public static TemplateImpl createTemplate( String emailTemplate )
    {
        TemplateImpl template = null;

        Matcher matcher = TEMPLATE_NAME_PATTERN.matcher( emailTemplate );
        if ( matcher.matches() )
        {
            String name = matcher.group( 1 );
            template = Resolver.getTemplate( name );

            if ( template == null )
            {
                template = new UnassignableTemplate();
            }

            log.debug( "Found template class " + template.getClass().getName() + " for template string " + emailTemplate );

            template.parseTemplate( emailTemplate );
        }

        return template;
    }

    @Override
    public String getApiKey()
    {
        return apiKey;
    }

    @Override
    public void setApiKey( String apiKey )
    {
        this.apiKey = apiKey;
    }

    @Override
    public List<Attachment> getAttachments()
    {
        return attachments;
    }

    @Override
    public void setAttachments( List<Attachment> attachments )
    {
        this.attachments = attachments;
    }

    @Override
    public final String toString()
    {
        ToStringBuilder sb = new TemplateReflectionToStringBuilder( this, ToStringStyle.MULTI_LINE_STYLE );
        sb.append( "Template", getFullTemplateName() );
        return sb.toString();
    }

    public final String getFullTemplateName()
    {
        return String.format( "%s-%s", getTemplateName(), getTemplateVersion() );
    }

    /*
     * Supports reflective toString() implementation for all TemplateImpl subclasses.
     */
    private static class TemplateReflectionToStringBuilder extends ReflectionToStringBuilder
    {
        private TemplateReflectionToStringBuilder( Object object, ToStringStyle toStringStyle )
        {
            super( object, toStringStyle );
            setExcludeFieldNames( new String[]{ "TEMPLATE_KEY_PATTERN" } );
        }

        @Override
        protected Object getValue( Field field ) throws IllegalArgumentException, IllegalAccessException
        {
            if ( TemplateImpl.class.isAssignableFrom( field.getType() ) )
            {
                TemplateImpl template = ( TemplateImpl ) super.getValue( field );
                return new TemplateReflectionToStringBuilder( template, ToStringStyle.DEFAULT_STYLE ).toString();
            }
            else
            {
                return super.getValue( field );
            }
        }
    }

    static Function<String, String> normalizeKeyFunction = TemplateImpl::normalizeKey;
}
