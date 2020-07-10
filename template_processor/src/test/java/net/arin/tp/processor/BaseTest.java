package net.arin.tp.processor;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import net.arin.tp.api.executors.ClientFactory;
import net.arin.tp.api.payload.ErrorPayload;
import net.arin.tp.api.payload.Payload;
import net.arin.tp.api.payload.PayloadList;
import net.arin.tp.processor.action.Processor;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.util.MockContext;
import net.arin.tp.processor.util.MockQueue;
import net.arin.tp.processor.utils.ServiceLocator;
import net.arin.tp.utils.Constants;
import net.arin.tp.utils.SystemPropertiesHelper;
import net.arin.tp.utils.TemplateProcessorProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.easymock.EasyMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.spi.NamingManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public abstract class BaseTest
{
    protected static final Logger log = LoggerFactory.getLogger( BaseTest.class );
    private HttpClient httpClient;
    private GreenMail mailServer = null;

    private static String bindAddress;

    static
    {
        String ba = SystemPropertiesHelper.getProperty( "jboss.bind.address" );

        bindAddress = Objects.requireNonNullElse( ba, "localhost" );
    }

    private byte[] marshal( Class clazz, Object object )
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance( clazz );
            Marshaller marshaller = jc.createMarshaller();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            marshaller.marshal( object, out );
            return out.toByteArray();
        }
        catch ( JAXBException e )
        {
            throw new RuntimeException( e );
        }
    }

    @BeforeSuite
    public void setupJndi() throws Exception
    {
        int portOffset = 49152 + RandomUtils.nextInt( 16382 );
        ServerSetup smtp = new ServerSetup( 25 + portOffset, null, ServerSetup.PROTOCOL_SMTP );

        mailServer = new GreenMail( smtp );
        mailServer.start();

        final Context ctx = new MockContext();
        if ( !NamingManager.hasInitialContextFactoryBuilder() )
        {
            NamingManager.setInitialContextFactoryBuilder( environment -> environment1 -> ctx );
        }

        System.setProperty( ServiceLocator.BASE_URL_JNDI_PATH, "http://bogus.example:8080" );

        // Create a mock mail session.
        Properties mailProps = new Properties();
        mailProps.setProperty( "mail.smtp.host", bindAddress );
        mailProps.setProperty( "mail.smtp.port", String.valueOf( smtp.getPort() ) );
        mailProps.setProperty( "mail.transport.protocol", "smtp" );
        javax.mail.Session session = javax.mail.Session.getDefaultInstance( mailProps, null );
        System.getProperties().put( "mailSession", session );

        System.setProperty( TemplateProcessorProperties.PROP_MIMETYPES_IGNORE, "text/fubar" );

        ctx.bind( Constants.ROUTER_QUEUE, MockQueue.getInstance( Constants.ROUTER_QUEUE ) );
        ctx.bind( Constants.TEMPLATE_QUEUE, MockQueue.getInstance( Constants.TEMPLATE_QUEUE ) );

        // Set the response to only queue messages; don't try to deliver them.
        Response.setQueueOnly( true );

        // Need to mock what the Startup MBean is doing.
        Resolver.initialize();
    }

    @AfterSuite
    public void stopMailServer()
    {
        if ( mailServer != null )
        {
            mailServer.stop();
        }
    }

    @AfterMethod
    public void resetHttpClient()
    {
        if ( httpClient != null )
        {
            EasyMock.verify( httpClient );

            httpClient = null;
            ClientFactory.setHttpClient( null );
        }
    }

    @AfterMethod
    public void clearResponseMessageQueue()
    {
        Response.getMessageQueue().clear();
        MockQueue.reset();
    }

    private void replay()
    {
        EasyMock.replay( httpClient );
    }

    protected void createResponse( int statusCode, Payload payload ) throws IOException
    {
        if ( httpClient == null )
        {
            // Set up the mocked client.
            httpClient = EasyMock.createMock( HttpClient.class );
            ClientFactory.setHttpClient( httpClient );
        }

        byte[] content = marshal( payload.getClass(), payload );
        HttpResponse response = createHttpResponse( statusCode, content );
        EasyMock.expect( httpClient.execute( EasyMock.<HttpRequestBase>anyObject(), EasyMock.<HttpContext>anyObject() ) ).andReturn( response );
    }

    protected <T extends Payload> void createPayloadListResponse( int statusCode, PayloadList<T> payload )
            throws IOException
    {
        if ( httpClient == null )
        {
            // Set up the mocked client.
            httpClient = EasyMock.createMock( HttpClient.class );
            ClientFactory.setHttpClient( httpClient );
        }

        byte[] content = marshal( payload.getClass(), payload );
        HttpResponse response = createHttpResponse( statusCode, content );
        EasyMock.expect( httpClient.execute( EasyMock.<HttpRequestBase>anyObject(), EasyMock.<HttpContext>anyObject() ) ).andReturn( response );
    }

    private HttpResponse createHttpResponse( int statusCode, byte[] content )
    {
        StatusLine status = new BasicStatusLine( HttpVersion.HTTP_1_1, statusCode, "" );
        HttpEntity entity = new ByteArrayEntity( content );
        HttpResponse response = new BasicHttpResponse( status );
        response.setEntity( entity );
        return response;
    }

    protected javax.jms.Message route( TemplateMessage templateMessage )
    {
        try
        {
            Processor processor = new Processor();
            processor.onTemplate( templateMessage );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }

        return null;
    }

    protected void setupMock( SetupAssistant assistant ) throws Exception
    {
        // Set up the responses to the core service calls of whatever is being tested.
        assistant.coreSetup();

        replay();
    }

    protected MimeMessage getMimeMessage( String from, String to, String cc, String subject, String body,
                                          List<String> attachments, Map<String, String> headers )
    {
        MimeMessage mimeMessage = new MimeMessage( javax.mail.Session.getDefaultInstance( new Properties() ) );

        MimeMultipart multipart = new MimeMultipart();

        try
        {
            mimeMessage.setFrom( new InternetAddress( from ) );
            mimeMessage.setRecipient( MimeMessage.RecipientType.TO, new InternetAddress( to ) );

            if ( cc != null )
            {
                mimeMessage.setRecipient( MimeMessage.RecipientType.CC, new InternetAddress( cc ) );
            }

            mimeMessage.setSubject( subject );

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText( body );
            multipart.addBodyPart( textPart );

            if ( attachments != null )
            {
                for ( int i = 0; i < attachments.size(); i++ )
                {
                    String filename = "attachment" + i + ".txt";

                    MimeBodyPart template = new MimeBodyPart();
                    template.setFileName( filename );
                    template.setContent( attachments.get( i ), "text/plain" );
                    multipart.addBodyPart( template );
                }
            }

            for ( Map.Entry<String, String> entry : headers.entrySet() )
            {
                mimeMessage.addHeader( entry.getKey(), entry.getValue() );
            }
        }
        catch ( Exception e )
        {
            log.error( "", e );
        }

        try
        {
            mimeMessage.setContent( multipart );
        }
        catch ( Exception e )
        {
            log.error( "", e );
        }

        return mimeMessage;
    }

    protected interface SetupAssistant
    {
        void coreSetup() throws Exception;

        void requestSetup() throws Exception;

        void responseSetup() throws Exception;
    }

    /**
     * A partially implemented SetupAssistant that assumes monitoring is turned off.
     */
    protected abstract class AbstractSetupAssistant implements SetupAssistant
    {
        @Override
        public void requestSetup()
        {
        }

        @Override
        public void responseSetup()
        {
        }
    }

    protected ErrorPayload getErrorPayload( String message )
    {
        ErrorPayload errorPayload = new ErrorPayload();
        errorPayload.setMessage( message );

        return errorPayload;
    }

    protected String assertPresenceOfMessageInQueueAndReturnContent() throws Exception
    {
        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        return ( String ) response.getContent();
    }

    /*
     * When templates are parsed, multiline labelled fields are treated differently than multiline unlabelled fields.
     * Unlabelled fields will end up in the same cell of a list (with embedded newline) whereas labelled fields will
     * each get their own cell in the list. (Think of it this way: one list element per label)
     *
     * Given a field that was parsed as described above, this method returns a list where each line gets its own cell.
     */
    protected List<String> normalizeMultilineTemplateProperty( List<String> property )
    {
        return Arrays.asList( StringUtils.split( StringUtils.join( property, "\n" ), "\n" ) );
    }
}
