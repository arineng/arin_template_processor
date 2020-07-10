package net.arin.tp.processor.message;

import net.arin.tp.processor.template.TemplateImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class MailMessage extends RegcoreMessage implements Serializable
{
    private static final Logger log = LoggerFactory.getLogger( MailMessage.class );

    private static final String TEMPLATE_MIMETYPE = "text/plain";

    private ArrayList<Part> parts = new ArrayList<>();
    private Map<String, String> headers = new HashMap<>();

    private String original;

    public MailMessage( MimeMessage mail )
    {
        try
        {
            for ( Enumeration<Header> e = mail.getAllHeaders(); e.hasMoreElements(); )
            {
                Header h = e.nextElement();
                headers.put( h.getName(), h.getValue() );
            }

            original = serialize( mail );

            if ( mail.getFrom() != null )
            {
                for ( Address address : mail.getFrom() )
                {
                    getFrom().add( address.toString() );
                }
            }

            if ( mail.getReplyTo() != null )
            {
                for ( Address address : mail.getReplyTo() )
                {
                    getReplyTo().add( address.toString() );
                }
            }

            try
            {
                if ( mail.getRecipients( javax.mail.Message.RecipientType.TO ) != null )
                {
                    for ( Address address : mail.getRecipients( javax.mail.Message.RecipientType.TO ) )
                    {
                        getTo().add( address.toString() );
                    }
                }
            }
            catch ( AddressException e )
            {
                log.debug( "Try getting recipients from To header instead of To field" );
                List<String> recipients = getRecipientsUsingHeader( mail, "To" );
                for ( String recipient : recipients )
                {
                    getTo().add( recipient );
                }
            }

            try
            {
                if ( mail.getRecipients( javax.mail.Message.RecipientType.CC ) != null )
                {
                    for ( Address address : mail.getRecipients( javax.mail.Message.RecipientType.CC ) )
                    {
                        getCc().add( address.toString() );
                    }
                }
            }
            catch ( AddressException e )
            {
                log.debug( "Try getting recipients from Cc header instead of Cc field" );
                List<String> recipients = getRecipientsUsingHeader( mail, "Cc" );
                for ( String recipient : recipients )
                {
                    getCc().add( recipient );
                }
            }

            setSubject( mail.getSubject() );

            Object content = mail.getContent();

            if ( content instanceof Multipart )
            {
                Multipart multipartContent = ( Multipart ) content;

                for ( int i = 0; i < multipartContent.getCount(); i++ )
                {
                    javax.mail.Part part = multipartContent.getBodyPart( i );

                    parts.add( new Part( part ) );
                }
            }
            else
            {
                parts.add( new Part( mail ) );
            }
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    private List<String> getRecipientsUsingHeader( MimeMessage mail, String header )
            throws MessagingException
    {
        List<String> validAddresses = new ArrayList<>();
        if ( mail.getHeader( header ) != null && mail.getHeader( header ).length != 0 )
        {
            for ( String headerVal : mail.getHeader( header ) )
            {
                String[] allAddresses = headerVal.split( ";" );
                for ( String address : allAddresses )
                {
                    if ( address != null && !address.isEmpty() )
                    {
                        validAddresses.add( address );
                    }
                }
            }
        }
        return validAddresses;
    }

    public ArrayList<Part> getParts()
    {
        return parts;
    }

    public void setParts( ArrayList<Part> parts )
    {
        this.parts = parts;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public void setHeaders( Map<String, String> headers )
    {
        this.headers = headers;
    }

    public MimeMessage constructMimeMessage( Session session )
            throws MessagingException
    {
        ByteArrayInputStream stream;

        if ( original == null )
        {
            return null;
        }

        stream = unserialize( original );

        MimeMessage message = new MimeMessage( session, stream );

        for ( Map.Entry<String, String> entry : headers.entrySet() )
        {
            message.setHeader( entry.getKey(), entry.getValue() );
        }

        return message;
    }

    /**
     * This will return true if any part of the message is a template. In the case of multipart messages, we'll loop
     * through all the parts and return true as soon as we find a single part that matches the template mimetype and
     * contains the Template:.*END OF TEMPLATE text.
     */
    public boolean getAnyPartTemplate()
    {
        List<Part> parts = getParts();

        for ( Part part : parts )
        {
            if ( part.getContentType().equalsIgnoreCase( TEMPLATE_MIMETYPE )
                    && isTemplate( part.getContentAsString() ) )
            {
                return true;
            }
        }

        return false;
    }

    private String serialize( MimeMessage message )
            throws IOException, MessagingException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        message.writeTo( stream );

        byte[] raw = stream.toByteArray();
        byte[] encoded = Base64.encodeBase64( raw );

        return new String( encoded );
    }

    private ByteArrayInputStream unserialize( String string )
    {
        byte[] encoded = string.getBytes();
        byte[] raw = Base64.decodeBase64( encoded );

        return new ByteArrayInputStream( raw );
    }

    private boolean isTemplate( String content )
    {
        if ( content == null || StringUtils.isEmpty( content ) )
        {
            return false;
        }

        Matcher matcher = TemplateImpl.TEMPLATE_CHECK_PATTERN.matcher( content );

        return matcher.find();
    }

    public String getMessageAsBase64()
    {
        return original;
    }

    public class Part implements Serializable
    {
        private String filename = null;
        private String contentType = null;
        private byte[] content = null;

        public Part()
        {
        }

        Part( javax.mail.Part part )
        {
            try
            {
                filename = part.getFileName();
            }
            catch ( Exception e )
            {
                log.warn( "Unable to get file name", e );
            }
            finally
            {
                if ( filename == null )
                {
                    filename = "";
                }
            }

            try
            {
                contentType = part.getContentType();
            }
            catch ( Exception e )
            {
                log.warn( "Unable to get content type", e );
            }
            finally
            {
                if ( contentType == null )
                {
                    contentType = "";
                }
            }

            String[] tmp = contentType.split( ";" );

            contentType = tmp[0];

            try
            {
                if ( contentType.equalsIgnoreCase( TEMPLATE_MIMETYPE ) )
                {
                    String text = ( String ) part.getContent();

                    // If it's text, we'll treat it like a string.
                    content = text.getBytes();
                }
                else
                {
                    // If it's anything else, we'll just read the input stream and toss it into a byte array for later.
                    content = IOUtils.toByteArray( part.getInputStream() );
                }
            }
            catch ( Exception e )
            {
                log.warn( "Unable to get content", e );
            }
        }

        public String getContentType()
        {
            return contentType;
        }

        public void setContentType( String contentType )
        {
            this.contentType = contentType;
        }

        public byte[] getContent()
        {
            return content;
        }

        public String getContentAsString()
        {
            return new String( content );
        }

        public void setContent( byte[] content )
        {
            this.content = content;
        }

        public String getFilename()
        {
            return filename;
        }

        public void setFilename( String filename )
        {
            this.filename = filename;
        }

        public boolean isTemplateMimeType()
        {
            return getContentType().equalsIgnoreCase( TEMPLATE_MIMETYPE );
        }
    }
}
