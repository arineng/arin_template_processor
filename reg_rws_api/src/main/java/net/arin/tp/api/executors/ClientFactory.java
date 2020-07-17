package net.arin.tp.api.executors;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * This factory allows tests to replace the {@link DefaultHttpClient} with a mocked client.
 */
public class ClientFactory
{
    private static HttpClient client;

    public static HttpClient createHttpClient()
    {
        HttpClient retval;
        if ( client != null )
        {
            retval = client;
            return retval;
        }

        return new DefaultHttpClient();
    }

    public static void setHttpClient( HttpClient httpClient )
    {
        client = httpClient;
    }
}
