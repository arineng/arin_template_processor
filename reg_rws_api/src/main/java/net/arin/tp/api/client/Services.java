package net.arin.tp.api.client;

import net.arin.tp.api.service.CustomerService;
import net.arin.tp.api.service.NetService;
import net.arin.tp.api.service.OrgService;
import net.arin.tp.api.service.PocService;
import net.arin.tp.api.service.Service;
import org.jboss.resteasy.client.ProxyFactory;

import java.util.HashMap;

/**
 * This class serves as an entry point for clients to access ARIN registration services.
 */
public final class Services
{
    private static final Services arinServices = new Services( "www.arin.net", 80, true );

    private final String baseUrl;
    private final HashMap<Class<? extends Service>, Service> serviceCache = new HashMap<>();

    /**
     * Return the singleton Services instance that always points to ARIN at https://www.arin.net.
     *
     * @return the official ARIN registration services client object
     */
    public static Services arin()
    {
        return arinServices;
    }

    /**
     * Create a new Services instance that points to a registration services web service at a specific location.
     *
     * @param host  the host of the web server
     * @param port  the TCP port of the server
     * @param https true for https, false for http
     */
    public Services( String host, int port, boolean https )
    {
        baseUrl = String.format( "%s://%s:%d/regcore/seam/resource/rest", https ? "https" : "http", host, port );
    }

    private String getBaseUrl()
    {
        return baseUrl;
    }

    public PocService getPocService()
    {
        return getService( PocService.class );
    }

    public OrgService getOrgService()
    {
        return getService( OrgService.class );
    }

    public NetService getNetService()
    {
        return getService( NetService.class );
    }

    public CustomerService getCustomerService()
    {
        return getService( CustomerService.class );
    }

    private <T extends Service> T getService( Class<T> serviceClass )
    {
        @SuppressWarnings( "unchecked" )
        T service = ( T ) serviceCache.get( serviceClass );
        if ( service == null )
        {
            service = ProxyFactory.create( serviceClass, getBaseUrl() );
            serviceCache.put( serviceClass, service );
        }
        return service;
    }
}
