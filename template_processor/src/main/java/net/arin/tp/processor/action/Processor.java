package net.arin.tp.processor.action;

import net.arin.tp.utils.TemplateProcessorProperties;
import net.arin.tp.processor.Resolver;
import net.arin.tp.processor.action.converter.MailToTemplate;
import net.arin.tp.processor.action.converter.MessageConversionException;
import net.arin.tp.processor.exception.TemplateRequiresReviewException;
import net.arin.tp.processor.message.MailMessage;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.transform.Transformer;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

@Startup
@Singleton( name = "processor" )
@ConcurrencyManagement( ConcurrencyManagementType.BEAN )
@TransactionManagement( TransactionManagementType.BEAN )
public class Processor
{
    static final private Logger log = LoggerFactory.getLogger( Processor.class );

    static final private String NOT_APPLICABLE = "N/A";

    // POP3 is the protocol used to poll incoming email.
    static final private String POLLER_PROTOCOL = "pop3";

    private ValidateTemplate validateTemplate = new ValidateTemplate();

    private LRUMap failedMessageMap;

    @Resource( name = "java:/mail/RegcorePoller" )
    Session mailSession;

    @PostConstruct
    public void initialize()
    {
        logProperties();
        Resolver.initialize();
        failedMessageMap = new LRUMap( 100 );
    }

    private void logProperties()
    {
        log.info( "Template Processor properties: " );
        logProperty( TemplateProcessorProperties.PROP_HOSTMASTER_NAME );
        logProperty( TemplateProcessorProperties.PROP_HOSTMASTER_EMAIL );
        logProperty( TemplateProcessorProperties.PROP_POLLER_FOLDER );
        logProperty( TemplateProcessorProperties.PROP_REGRWS_URL );
        logProperty( TemplateProcessorProperties.PROP_API_KEY );
        logProperty( TemplateProcessorProperties.PROP_MIMETYPES_IGNORE );
        logProperty( TemplateProcessorProperties.PROP_MAIL_DEBUG );
        logProperty( TemplateProcessorProperties.PROP_MAIL_SERVER_URI );
        logProperty( TemplateProcessorProperties.PROP_MAIL_START_TLS );
        logProperty( TemplateProcessorProperties.PROP_BASEURL_PUBLIC );
    }

    private void logProperty( String property )
    {
        log.info( property + "=" + TemplateProcessorProperties.getProperty( property ) );
    }

    @Schedule( minute = "*/1", hour = "*", persistent = false )
    public void trigger()
    {
        log.info( "Polling messages..." );

        Store store = null;
        Folder inbox = null;
        try
        {
            store = mailSession.getStore( POLLER_PROTOCOL );
            store.connect();

            String folder = TemplateProcessorProperties.getProperty( TemplateProcessorProperties.PROP_POLLER_FOLDER );
            inbox = store.getFolder( folder );
            if ( inbox == null )
            {
                throw new RuntimeException( "Folder " + folder + " not found" );
            }
            inbox.open( Folder.READ_WRITE );

            SearchTerm searchTerm = new AndTerm(
                    new FlagTerm( new Flags( Flags.Flag.DELETED ), false ),
                    new FlagTerm( new Flags( Flags.Flag.SEEN ), false )
            );

            Message[] messages = inbox.search( searchTerm );
            log.info( "Message count: " + messages.length );
            for ( Message message : messages )
            {
                long start = new Date().getTime();

                String fromAddress;
                try
                {
                    fromAddress = getFrom( message );
                }
                catch ( Exception e )
                {
                    log.warn( "Exception when parsing From address: " + e.getMessage() );
                    logFailedMessage( "Message with bad From address:", message );
                    markAsRead( message );
                    continue;
                }

                String messageId = getMessageId( message );

                log.debug( "Handling message: id=" + messageId + " from=" + fromAddress );

                try
                {
                    if ( message instanceof MimeMessage )
                    {
                        MailMessage rcm = new MailMessage( ( MimeMessage ) message );

                        if ( rcm.getAnyPartTemplate() )
                        {
                            onTemplate( rcm );
                        }
                        else
                        {
                            log.info( "Message is not a template" );
                            logMimeMessage( rcm.constructMimeMessage( mailSession ) );
                        }
                    }
                    else
                    {
                        log.warn( "Not a MIME message: " + message );
                    }

                    markAsRead( message );
                }
                catch ( Exception e )
                {
                    log.warn( "id=" + messageId + " from=" + fromAddress + " - Unable to read message", e );
                    logFailedMessage( "Message that could not be read:", message );
                    markAsRead( message );
                }

                long duration = new Date().getTime() - start;
                log.info( String.format( "Message processed in %d ms", duration ) );
            }
        }
        catch ( Exception e )
        {
            log.error( "Could not poll messages: " + e.getMessage(), e );
        }
        finally
        {
            if ( inbox != null )
            {
                try
                {
                    inbox.close( true );
                }
                catch ( Exception e )
                {
                    log.warn( "", e );
                }
            }

            if ( store != null )
            {
                try
                {
                    store.close();
                }
                catch ( Exception e )
                {
                    log.warn( "", e );
                }
            }
        }
    }

    @SuppressWarnings( "unchecked" )
    private void logFailedMessage( String preamble, Message message )
    {
        String messageId = getMessageId( message );
        if ( !failedMessageMap.containsKey( messageId ) )
        {
            failedMessageMap.put( messageId, 1 );
            String messageString = getMessageString( message );
            if ( messageString != null )
            {
                log.warn( preamble );
                log.warn( messageString );
            }
        }
        else
        {
            log.warn( "Message with ID " + messageId + " has already failed to process" );
        }
    }

    private String getMessageString( Message message )
    {
        String messageString = null;

        if ( message instanceof MimeMessage )
        {
            try
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                message.writeTo( stream );
                byte[] raw = stream.toByteArray();
                messageString = new String( raw );
            }
            catch ( Exception e )
            {
                log.warn( "Exception when getting message string: " + e.getMessage() );
            }
        }
        else
        {
            log.warn( "Could not get message string because not a MIME message" );
        }

        return messageString;
    }

    /**
     * Retrieves Message-ID from the header of a mail message.
     */
    private String getMessageId( Message mimeMessage )
    {
        if ( mimeMessage != null )
        {
            try
            {
                String[] tmp = mimeMessage.getHeader( "Message-ID" );

                if ( tmp == null || tmp.length < 1 )
                {
                    return NOT_APPLICABLE;
                }

                String messageId = tmp[0];

                // A little clean up.
                messageId = messageId.replaceAll( "<", "" );
                messageId = messageId.replaceAll( ">", "" );

                return messageId;
            }
            catch ( Exception e )
            {
                log.error( "Could not retrieve Message-ID from mail", e );
            }
        }

        return NOT_APPLICABLE;
    }

    /**
     * Retrieves the From address from a mail message.
     */
    private String getFrom( Message mimeMessage )
            throws MessagingException
    {
        if ( mimeMessage != null )
        {
            InternetAddress[] from = ( InternetAddress[] ) mimeMessage.getFrom();

            if ( from == null || from.length < 1 || from[0] == null || from[0].getAddress() == null )
            {
                return NOT_APPLICABLE;
            }

            return from[0].getAddress();
        }

        return NOT_APPLICABLE;
    }

    /**
     * So we do not process the email again, mark message as "READ".
     *
     * @param message  email message
     * @throws javax.mail.MessagingException error setting read flag
     */
    private void markAsRead( Message message )
            throws MessagingException
    {
        // POP3 only supports delete.
        message.setFlag( Flags.Flag.DELETED, true );
    }

    private void onTemplate( MailMessage mailMessage )
    {
        MailToTemplate mailToTemplate = new MailToTemplate();
        List<TemplateMessage> templates;

        try
        {
            templates = mailToTemplate.convert( mailMessage );
        }
        catch ( MessageConversionException e )
        {
            createAndSendGeneralFailure( e.getTemplate(), e.getMessage() );

            return;
        }

        for ( TemplateMessage template : templates )
        {
            onTemplate( template );
        }
    }

    public void onTemplate( TemplateMessage templateMessage )
    {
        if ( !validateTemplate.validate( templateMessage ) )
        {
            // The validate routine will send an email if there's a failure. All we need to do here is return so we
            // don't continue to process the message.
            return;
        }

        Transformer transformer = Resolver.getTransformer( templateMessage.getTemplate() );
        if ( transformer != null )
        {
            try
            {
                String templateName = templateMessage.getTemplate().getTemplateName();
                if ( StringUtils.equals( templateName, "ARIN-POC" )
                        || StringUtils.equals( templateName, "ARIN-ORG" )
                        || StringUtils.equals( templateName, "ARIN-REASSIGN-SIMPLE" )
                        || StringUtils.equals( templateName, "ARIN-REASSIGN-DETAILED" )
                        || StringUtils.equals( templateName, "ARIN-IPv6-REASSIGN" )
                        || StringUtils.equals( templateName, "ARIN-REALLOCATE" )
                        || StringUtils.equals( templateName, "ARIN-IPv6-REALLOCATE" )
                        || StringUtils.equals( templateName, "ARIN-NET-MOD" )
                        || StringUtils.equals( templateName, "ARIN-IPv6-NET-MOD" ) )
                {
                    transformer.singleRESTCallTransform( templateMessage );
                }
                else
                {
                    log.warn( "Unsupported template: " + templateName );
                }
            }
            catch ( TemplateRequiresReviewException e )
            {
                log.info( "", e );
                logTemplateMessage( templateMessage );
            }
            catch ( RuntimeException e )
            {
                if ( e.getCause() instanceof HttpHostConnectException )
                {
                    throw new IllegalStateException( "ARIN Reg-RWS is unavailable" );
                }
                else
                {
                    throw e;
                }
            }
        }
        else
        {
            logTemplateMessage( templateMessage );
        }
    }

    /**
     * Everywhere else in the system, transformers assemble the response and return that message back to the
     * singleRESTCallTransform method. But, from this class, we'll need to create and send the error when it occurs.
     */
    private void createAndSendGeneralFailure( TemplateMessage template, String error )
    {
        try
        {
            javax.mail.Message response = Response.generalFailure( template, error );
            Response.send( response );
        }
        catch ( Exception e )
        {
            log.error( "Unknown error occurred while sending general failure", e );
        }
    }

    private void logTemplateMessage( TemplateMessage templateMessage )
    {
        log.info( "Template message details:" );
        String subject = templateMessage.getOriginalMessage().getSubject();
        if ( !templateMessage.getFrom().isEmpty() )
        {
            log.info( "From: " + templateMessage.getFrom().get( 0 ) );
        }
        else
        {
            log.info( "Missing From address" );
        }
        log.info( "Subject: " + subject );
        log.info( "Attachment count: " + templateMessage.getTemplate().getAttachments().size() );
        log.info( "Template: " + templateMessage.getText() );

        logMimeMessage( new MimeMessage( mailSession ) );
    }

    private void logMimeMessage( MimeMessage message )
    {
        try
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            message.writeTo( stream );

            byte[] raw = stream.toByteArray();

            log.info( "MIME message details:" );
            log.info( new String( raw ) );
        }
        catch ( Exception e )
        {
            log.debug( "Unable to log MIME message details", e );
        }
    }
}
