package net.arin.tp.processor.response;

import net.arin.tp.mail.PlainSendMailSync;
import net.arin.tp.api.payload.CustomerPayload;
import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.OrgPayload;
import net.arin.tp.api.payload.PocPayload;
import net.arin.tp.api.payload.TicketPayload;
import net.arin.tp.processor.action.xheader.XRegcoreStatusAppender;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.utils.DateTimeFormatter;
import net.arin.tp.utils.SystemPropertiesHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Used to send email responses back to the customer.
 */
public class Response
{
    private static final Logger log = LoggerFactory.getLogger( Response.class );

    private static final String MAIL_SESSION_JNDI_PATH = "java:/mail/RegcoreMailForward";
    private static final String HOSTMASTER_NAME_PROP = "arin.template.processor.hostmaster_name";
    private static final String HOSTMASTER_EMAIL_PROP = "arin.template.processor.hostmaster_email";

    // Used for testing.
    private static boolean queueOnly = false;
    private static final List<Message> messageQueue = new ArrayList<>();

    // Setup the Velocity engine.
    static
    {
        try
        {
            Velocity.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );
            Velocity.setProperty( "runtime.log.logsystem.log4j.logger", log.getName() );
            Velocity.setProperty( "resource.loader", "class" );
            Velocity.setProperty( "class.resource.loader.class", ClasspathResourceLoader.class.getName() );
            Velocity.setProperty( "velocimacro.library", "email/globalLibrary.vm" );
            Velocity.init();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Set the transport to only queue the messages, don't actually try to deliver them. This should only be used in
     * testing!
     *
     * @param queueOnly boolean indicating whether the messages should only be queued, and not delivered
     */
    public static void setQueueOnly( boolean queueOnly )
    {
        Response.queueOnly = queueOnly;
    }

    /**
     * Get a list of the queued messages. This should only be used in testing!
     *
     * @return a list of queued messages
     */
    public static List<Message> getMessageQueue()
    {
        return messageQueue;
    }

    public static Message generalFailure( TemplateMessage templateMessage, String... errors )
    {
        return generalFailure( templateMessage, Arrays.asList( errors ) );
    }

    public static Message generalFailure( TemplateMessage templateMessage, List<String> errors )
    {
        try
        {
            return createResponse( templateMessage, new GeneralFailureResponseContext( errors ) );
        }
        catch ( MessagingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Message pocSuccessful( TemplateMessage templateMessage, PocPayload payload )
    {
        try
        {
            return createResponse( templateMessage, new PocResponseContext( payload ) );
        }
        catch ( MessagingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Message netSuccessful( TemplateMessage templateMessage, NetPayload payload )
    {
        try
        {
            return createResponse( templateMessage, new NetResponseContext( payload ) );
        }
        catch ( MessagingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Message netDeleteTicketed( TemplateMessage templateMessage, TicketPayload ticketPayload,
                                             NetPayload netPayload )
    {
        try
        {
            return createResponse( templateMessage, new TicketedNetDeleteResponseContext( ticketPayload, netPayload ) );
        }
        catch ( MessagingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Message createOrgSuccessful( TemplateMessage templateMessage, TicketPayload payload )
    {
        try
        {
            return createResponse( templateMessage, new TicketedOrgResponseContext( payload ) );
        }
        catch ( MessagingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Message orgSuccessful( TemplateMessage templateMessage, OrgPayload payload )
    {
        try
        {
            return createResponse( templateMessage, new OrgResponseContext( payload ) );
        }
        catch ( MessagingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Message simpleReassignSuccessful( TemplateMessage templateMessage, NetPayload netPayload,
                                                    CustomerPayload customerPayload )
    {
        try
        {
            return createResponse( templateMessage,
                    new SimpleReassignResponseContext( netPayload, customerPayload ) );
        }
        catch ( MessagingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Message detailedReassignSuccessful( TemplateMessage templateMessage, NetPayload netPayload,
                                                      OrgPayload orgPayload )
    {
        try
        {
            return createResponse( templateMessage,
                    new DetailedReassignResponseContext( netPayload, orgPayload ) );
        }
        catch ( MessagingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static Message reallocateSuccessful( TemplateMessage templateMessage, NetPayload netPayload,
                                                OrgPayload orgPayload )
    {
        try
        {
            return createResponse( templateMessage, new ReallocateResponseContext( netPayload, orgPayload ) );
        }
        catch ( MessagingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static void send( Message mailMessage )
    {
        if ( queueOnly )
        {
            messageQueue.add( mailMessage );
            return;
        }

        PlainSendMailSync plainSendMailSync = new PlainSendMailSync();
        plainSendMailSync.send( mailMessage );
    }

    private static StringWriter interpolateVelocityTemplate( String template, VelocityContext context )
    {
        try
        {
            StringWriter writer = new StringWriter();
            Template t = Velocity.getTemplate( template );
            t.merge( context, writer );
            return writer;
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    private static Message createResponse( TemplateMessage templateMessage, ResponseContext responseContext )
            throws MessagingException
    {
        Set<String> recipients = new HashSet<>();

        // We should first try to send the message to any Reply-To address defined. If we don't have any then we should
        // send it to the From address.
        if ( templateMessage.getReplyTo() != null && templateMessage.getReplyTo().size() > 0 )
        {
            recipients.addAll( templateMessage.getReplyTo() );
        }
        else if ( templateMessage.getFrom() != null )
        {
            recipients.addAll( templateMessage.getFrom() );
        }

        StringBuilder subject = new StringBuilder();
        subject.append( "Re: " );
        subject.append( templateMessage.getSubject() );

        if ( !responseContext.isTicketed() && responseContext.getResponseType() != null )
        {
            subject.append( " --" );
            subject.append( responseContext.getResponseType().toString() );
        }

        return createResponse( templateMessage, responseContext, recipients, subject.toString() );
    }

    private static Message createResponse( TemplateMessage templateMessage, ResponseContext responseContext,
                                           Set<String> recipients, String subject )
            throws MessagingException
    {
        VelocityContext context = new VelocityContext();
        context.put( "hostmasterEmail", getHostmasterEmail() );
        if ( templateMessage != null )
        {
            context.put( "templateMessage", templateMessage );
        }
        context.put( "dateTimeFormatter", new DateTimeFormatter() );

        responseContext.setup( context );

        Session session = getMailSession();
        Message response = new MimeMessage( session );
        response.setFrom( getFromAddress() );

        response.setSubject( subject );

        String hostmasterAddress = getHostmasterEmail();

        if ( recipients != null )
        {
            for ( String address : recipients )
            {
                InternetAddress iAddr = new InternetAddress( address );
                log.debug( "Adding recipient TO  " + address + " / " + iAddr.getAddress() + " if no match for [ " + hostmasterAddress + " ] " );
                if ( !iAddr.getAddress().equals( hostmasterAddress ) )
                {
                    response.addRecipient( Message.RecipientType.TO, iAddr );
                }
            }
        }
        // And CC the addresses that were CC'd but not hostmaster.
        if ( templateMessage != null && templateMessage.getCc() != null )
        {
            for ( String address : templateMessage.getCc() )
            {
                InternetAddress iAddr = new InternetAddress( address );
                log.debug( "Adding recipient CC  " + address + " / " + iAddr.getAddress() + " if no match for [ " + hostmasterAddress + " ] " );
                if ( !iAddr.getAddress().equals( hostmasterAddress ) )
                {
                    response.addRecipient( Message.RecipientType.CC, iAddr );
                }
            }
        }

        if ( responseContext.getResponseType() != null )
        {
            // On the way out, we'll add a status x-header which details whether we're accepting or rejecting.
            response.setHeader( XRegcoreStatusAppender.NAME, responseContext.getResponseType().toString() );
        }

        StringWriter writer = interpolateVelocityTemplate( responseContext.getTemplate(), context );
        response.setText( writer.toString() );

        return response;
    }

    private static Address getFromAddress()
    {
        try
        {
            InternetAddress address = new InternetAddress();
            address.setPersonal( getHostmasterName(), "UTF-8" );
            address.setAddress( getHostmasterEmail() );
            return address;
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static Session getMailSession()
    {
        try
        {
            return ( Session ) new InitialContext().lookup( MAIL_SESSION_JNDI_PATH );
        }
        catch ( NamingException e )
        {
            throw new RuntimeException( "Mail Session " + MAIL_SESSION_JNDI_PATH + " not configured" );
        }
    }

    private static String getHostmasterName()
    {
        return SystemPropertiesHelper.getProperty( HOSTMASTER_NAME_PROP );
    }

    private static String getHostmasterEmail()
    {
        return SystemPropertiesHelper.getProperty( HOSTMASTER_EMAIL_PROP );
    }
}
