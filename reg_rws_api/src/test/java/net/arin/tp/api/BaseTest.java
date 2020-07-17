package net.arin.tp.api;

import net.arin.tp.api.executors.ClientFactory;
import net.arin.tp.api.payload.Payload;
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
import org.testng.annotations.AfterMethod;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTest
{
    protected static final Logger log = LoggerFactory.getLogger( BaseTest.class );
    protected HttpClient httpClient;

    protected byte[] marshal( Class clazz, Object object )
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

    protected void replay()
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

    protected HttpResponse createHttpResponse( int statusCode, byte[] content )
    {
        StatusLine status = new BasicStatusLine( HttpVersion.HTTP_1_1, statusCode, "" );
        HttpEntity entity = new ByteArrayEntity( content );
        HttpResponse response = new BasicHttpResponse( status );
        response.setEntity( entity );
        return response;
    }

    /*
     * Convenience method for creating Lists of T's.
     */
    protected <T> List<T> listOf( T first, T... rest )
    {
        List<T> list = new ArrayList<>();
        list.add( first );

        for ( T value : rest )
        {
            list.add( value );
        }

        return list;
    }
}
