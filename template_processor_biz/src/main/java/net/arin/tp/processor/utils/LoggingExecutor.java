package net.arin.tp.processor.utils;

import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;

/**
 * This class will wrap a ClientExecutor and log all requests to it.
 */
public class LoggingExecutor implements ClientExecutor
{
    private ClientExecutor executor;
    private static final Logger log = LoggerFactory.getLogger( LoggingExecutor.class );

    LoggingExecutor( ClientExecutor executor )
    {
        this.executor = executor;
    }

    @Override
    public ClientResponse execute( ClientRequest request ) throws Exception
    {
        log.info( "Reg-RWS request: " + request.getHttpMethod() + " " + request.getUri() );

        // Marshaling objects is expensive. Make sure debug output is being asked for before we log this.
        if ( log.isDebugEnabled() )
        {
            if ( request.getBody() != null )
            {
                log.debug( "Reg-RWS request body: " + marshal( request.getBodyType(), request.getBody() ) );
            }
        }

        return executor.execute( request );
    }

    public ClientRequest createRequest( String s )
    {
        return executor.createRequest( s );
    }

    public ClientRequest createRequest( UriBuilder uriBuilder )
    {
        return executor.createRequest( uriBuilder );
    }

    @Override
    public void close()
    {
    }

    private String marshal( Class clazz, Object object )
    {
        try
        {
            JAXBContext jc = JAXBContext.newInstance( clazz );
            Marshaller marshaller = jc.createMarshaller();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            marshaller.marshal( object, out );
            return new String( out.toByteArray() );
        }
        catch ( Throwable e )
        {
            // Swallow the exception. We're not bringing this thing down because of a log message.
            return "Error marshalling content for output to log file. Moving on.";
        }
    }
}
