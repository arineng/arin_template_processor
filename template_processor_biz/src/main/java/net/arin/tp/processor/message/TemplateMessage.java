package net.arin.tp.processor.message;

import net.arin.tp.processor.exception.MissingKeyException;
import net.arin.tp.processor.exception.MultipleKeyException;
import net.arin.tp.processor.template.Template;
import net.arin.tp.processor.template.TemplateImpl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * Provides access to the template-related information in a mail message.
 */
public class TemplateMessage extends RegcoreMessage implements Serializable
{
    private String text = "";
    private Template template = null;

    private MailMessage originalMessage;

    private Set<String> distinctApiKeys = new HashSet<>();

    public TemplateMessage()
    {
    }

    public TemplateMessage( MailMessage mailMessage )
    {
        if ( mailMessage.getSubject() != null )
        {
            setSubject( mailMessage.getSubject() );
        }

        // Add the addresses to the message.
        setFrom( mailMessage.getFrom() );
        setReplyTo( mailMessage.getReplyTo() );
        setTo( mailMessage.getTo() );
        setCc( mailMessage.getCc() );

        this.originalMessage = mailMessage;
    }

    public TemplateMessage( MailMessage mailMessage, String text )
    {
        this( mailMessage );
        setText( text );
    }

    public String getReplyText()
    {
        if ( getText() != null )
        {
            return getText().replaceAll( "(?m)^", "> " );
        }
        else
        {
            return null;
        }
    }

    public String getText()
    {
        return text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    public Template getTemplate()
    {
        if ( template == null && text != null )
        {
            template = TemplateImpl.createTemplate( text );
        }

        return template;
    }

    public void setTemplate( Template template )
    {
        this.template = template;
    }

    public MailMessage getOriginalMessage()
    {
        return originalMessage;
    }

    public String getApiKey()
    {
        findApiKeysInMessageComponents( distinctApiKeys, getFrom(), getSubject() );

        // Get from the template itself.
        if ( template.getApiKey() != null )
        {
            distinctApiKeys.add( template.getApiKey() );
        }

        if ( distinctApiKeys.size() > 1 )
        {
            throw new MultipleKeyException();
        }

        if ( distinctApiKeys.size() == 0 )
        {
            throw new MissingKeyException();
        }

        return distinctApiKeys.iterator().next();
    }

    /**
     * Find API keys based on a regular expression in the "from" addresses and subject passed in. If keys are found,
     * they are added to the collection passed into the method.
     *
     * @param apiKeys collection to add to if keys are found
     * @param from    list of "from" email addresses
     * @param subject email subject
     */
    private static void findApiKeysInMessageComponents( Collection<String> apiKeys, List<String> from, String subject )
    {
        // Get from the From address field.
        for ( String address : from )
        {
            Matcher matcher = TemplateImpl.API_KEY_PATTERN.matcher( address );
            if ( matcher.matches() )
            {
                apiKeys.add( matcher.group( 1 ) );
            }
        }

        // Get from the subject line.
        if ( subject != null )
        {
            Matcher matcher = TemplateImpl.API_KEY_PATTERN.matcher( subject );
            if ( matcher.matches() )
            {
                apiKeys.add( matcher.group( 1 ) );
            }
        }
    }
}
