package net.arin.tp.mail;

import net.arin.tp.mail.pojo.EmailAddress;
import net.arin.tp.mail.pojo.EmailAttachment;
import net.arin.tp.mail.pojo.MailPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Date;

public class PlainSendMailSync implements SendMailBase
{
    private static final Logger log = LoggerFactory.getLogger( PlainSendMailSync.class );

    public void send( MailPojo mailPojo )
    {
        log.trace( "PlainSendMailSync:send() start" );
        MailPropertiesUtil.MailProperties mailProperties = MailPropertiesUtil.getProperties();
        Session mailSession = Session.getInstance( mailProperties.properties );

        Message mimeMessage = mailPojoToMime( mailPojo, mailSession );

        send( mimeMessage );
    }

    public void send( Message unsignedMimeMessage )
    {
        MailPropertiesUtil.MailProperties mailProperties = MailPropertiesUtil.getProperties();
        Session mailSession = Session.getInstance( mailProperties.properties );
        sendEmail( unsignedMimeMessage, mailSession, mailProperties );
    }

    public void sendEmail( Message mimeMessage, Session mailSession, MailPropertiesUtil.MailProperties mailProperties )
    {
        sendEmail( mimeMessage, null, mailSession, mailProperties );
    }

    public void sendEmail( Message mimeMessage, Address[] recipients, Session mailSession, MailPropertiesUtil.MailProperties mailProperties )
    {
        try
        {
            sendEmailWithCheckedException( mimeMessage, recipients, mailSession, mailProperties );
        }
        catch ( MessagingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public void sendEmailWithCheckedException( Message mimeMessage, Address[] recipients, Session mailSession, MailPropertiesUtil.MailProperties mailProperties ) throws MessagingException
    {

        Transport transport;
        try
        {
            transport = mailSession.getTransport();
        }
        catch ( NoSuchProviderException e )
        {
            throw new RuntimeException( e );
        }

        transport.connect( mailProperties.usernamePassword.username, mailProperties.usernamePassword.password );

        try
        {
            if ( recipients != null )
            {
                transport.sendMessage( mimeMessage, recipients );
            }
            else
            {
                transport.sendMessage( mimeMessage, mimeMessage.getAllRecipients() );
            }

        }
        catch ( NullPointerException npe )
        {
            String fromAddress = null;
            try
            {
                if ( ( mimeMessage.getFrom() != null ) && mimeMessage.getFrom().length > 0 )
                {
                    Address from = mimeMessage.getFrom()[0];
                    fromAddress = from.toString();
                }
            }
            catch ( Exception e )
            {
                // Ignore.
            }

            log.error( "subject=" + mimeMessage.getSubject() + " from=" + fromAddress + " - Could not send message because of a missing required component" );
        }

        transport.close();
        log.trace( "PlainSendMailSync:send() end" );
    }

    private static MimeMessage mailPojoToMime( MailPojo mailPojo, Session mailSession )
    {
        try
        {
            MimeMessage mimeMessage = new MimeMessage( mailSession );

            mimeMessage.setSentDate( new Date() );

            mimeMessage.setFrom( mailPojo.getFromAddress().toInternetAddress() );
            mimeMessage.setReplyTo( internetAddresses( mailPojo.getReplyToAddresses() ) );

            mimeMessage.setRecipients( Message.RecipientType.TO, internetAddresses( mailPojo.getToAddresses() ) );
            mimeMessage.setRecipients( Message.RecipientType.CC, internetAddresses( mailPojo.getCcAddresses() ) );
            mimeMessage.setRecipients( Message.RecipientType.BCC, internetAddresses( mailPojo.getBccAddresses() ) );

            mimeMessage.setSubject( mailPojo.getSubject() );

            MimeMultipart multipart = new MimeMultipart();

            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText( mailPojo.getBody(), "UTF-8" );
            multipart.addBodyPart( bodyPart );

            for ( EmailAttachment attachment : mailPojo.getAttachments() )
            {
                ByteArrayDataSource dataSource = new ByteArrayDataSource( attachment.getContent(),
                        attachment.getContentType() );
                dataSource.setName( attachment.getName() );
                DataHandler dataHandler = new DataHandler( dataSource );

                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.setDataHandler( dataHandler );
                attachmentPart.setFileName( attachment.getName() );

                multipart.addBodyPart( attachmentPart );
            }

            // Add body.
            mimeMessage.setContent( multipart, "UTF-8" );

            return mimeMessage;
        }
        catch ( MessagingException e )
        {
            throw new RuntimeException( e );
        }
    }

    private static InternetAddress[] internetAddresses( Collection<EmailAddress> emailAddresses )
    {
        InternetAddress[] internetAddresses = new InternetAddress[emailAddresses.size()];

        int x = 0;
        for ( EmailAddress emailAddress : emailAddresses )
        {
            try
            {
                internetAddresses[x++] = new InternetAddress( emailAddress.getEmail(), emailAddress.getDisplayName() );
            }
            catch ( UnsupportedEncodingException e )
            {
                throw new RuntimeException( e );
            }
        }

        return internetAddresses;
    }
}
