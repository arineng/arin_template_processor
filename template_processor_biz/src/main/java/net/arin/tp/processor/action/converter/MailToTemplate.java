package net.arin.tp.processor.action.converter;

import net.arin.tp.processor.exception.MissingKeyException;
import net.arin.tp.processor.exception.MultipleKeyException;
import net.arin.tp.processor.exception.TemplateException;
import net.arin.tp.processor.message.MailMessage;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.template.Attachment;
import net.arin.tp.processor.template.Template;
import net.arin.tp.processor.template.TemplateImpl;
import net.arin.tp.utils.TemplateProcessorProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * This was done here so it could be unit tested. It used to be specified as a Template Processor action.
 */
public class MailToTemplate
{
    private static final Logger log = LoggerFactory.getLogger( MailToTemplate.class );

    private List<String> ignoredMimeTypes;

    public MailToTemplate()
    {
        // Load the ignored MIME types.
        String tmp = TemplateProcessorProperties.getProperty( TemplateProcessorProperties.PROP_MIMETYPES_IGNORE );
        if ( tmp != null && tmp.length() > 0 )
        {
            ignoredMimeTypes = Arrays.asList( tmp.split( "," ) );
        }
        else
        {
            ignoredMimeTypes = new ArrayList<>();
        }
    }

    /**
     * Takes a single MailMessage and converts it to one, or more, TemplateMessage objects.
     */
    public List<TemplateMessage> convert( MailMessage mailMessage )
            throws MessageConversionException
    {
        ArrayList<TemplateMessage> messages = new ArrayList<>();

        log.info( "Converting mail to template..." );

        List<String> templates = new ArrayList<>();
        List<MailMessage.Part> attachments = new ArrayList<>();

        for ( MailMessage.Part part : mailMessage.getParts() )
        {
            if ( part.isTemplateMimeType() )
            {
                String content = part.getContentAsString();

                if ( content == null || content.trim().equals( "" ) )
                {
                    log.debug( "Ignoring a possible template with no content" );
                    continue;
                }

                List<String> tmp = getTemplates( content );

                if ( tmp != null && tmp.size() > 0 )
                {
                    log.info( "Part with content type " + part.getContentType() + " to be added as one or more templates" );

                    // If we found templates then we should add them to our master list of templates to process.
                    templates.addAll( tmp );
                }
                else
                {
                    log.info( "Part with content type " + part.getContentType() + " to be added as an attachment" );

                    // If we didn't find any templates then we should add the part to our list of attachments.
                    attachments.add( part );
                }
            }
            else if ( ignoredMimeTypes.contains( part.getContentType() ) )
            {
                log.info( "Part with content type " + part.getContentType() + " ignored" );
            }
            else
            {
                // Anything other than expected template MIME type should just be added to the attachments list.
                log.info( "Part with content type " + part.getContentType() + " to be added as an attachment" );
                attachments.add( part );
            }
        }

        // If we have no templates, bail.
        if ( templates.size() == 0 )
        {
            log.error( "No templates found in mail message" );

            // Cause the action pipeline to stop processing.
            return null;
        }

        // If we have more than one template and any attachments, respond with error.
        if ( templates.size() > 1 && attachments.size() > 0 )
        {
            log.debug( "Found attachments where we also had multiple templates; sending error back" );
            throw new MessageConversionException( new TemplateMessage( mailMessage ),
                    "Attachments are only allowed when you submit a single template." );
        }

        // Prepare the List of Attachments for each template. This should only be done if there is one template to
        // process but if we change that rule later this will allow us to put the attachments onto each template in the
        // group.
        List<Attachment> templateAttachments = new ArrayList<>();
        for ( MailMessage.Part attachmentPart : attachments )
        {
            Attachment attachment = new Attachment();

            attachment.setContentType( attachmentPart.getContentType() );
            attachment.setFilename( attachmentPart.getFilename() );
            attachment.setData( attachmentPart.getContent() );

            templateAttachments.add( attachment );
        }

        // Create a Template message per template.
        for ( String body : templates )
        {
            TemplateMessage templateMessage = new TemplateMessage( mailMessage, body );

            try
            {
                Template template = templateMessage.getTemplate();

                if ( template == null )
                {
                    throw new MessageConversionException( templateMessage,
                            "Unknown template was found in your message." );
                }

                template.setAttachments( templateAttachments );

                String apiKey;
                try
                {
                    apiKey = templateMessage.getApiKey();
                }
                catch ( MultipleKeyException ex )
                {
                    throw new MessageConversionException( templateMessage, "Multiple API keys found." );
                }
                catch ( MissingKeyException ex )
                {
                    apiKey = TemplateProcessorProperties.getProperty( TemplateProcessorProperties.PROP_API_KEY );
                }
                if ( apiKey == null )
                {
                    throw new MessageConversionException( templateMessage, "No API keys found." );
                }
                template.setApiKey( apiKey.toUpperCase() );

                messages.add( templateMessage );
            }
            catch ( TemplateException e )
            {
                throw new MessageConversionException( templateMessage, e.getMessage() );
            }
        }

        return messages;
    }

    private List<String> getTemplates( String content )
    {
        List<String> templates = new ArrayList<>();
        Matcher matcher = TemplateImpl.TEMPLATE_CHECK_PATTERN.matcher( content );
        while ( matcher.find() )
        {
            templates.add( matcher.group( 0 ) );
        }
        return templates;
    }
}
