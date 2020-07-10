package net.arin.tp.processor.utils;

import net.arin.tp.api.executors.ClientFactory;
import net.arin.tp.api.service.CustomerService;
import net.arin.tp.api.service.NetService;
import net.arin.tp.api.service.OrgService;
import net.arin.tp.api.service.PocService;
import net.arin.tp.utils.SystemPropertiesHelper;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

public class ServiceLocator
{
    public static final String BASE_URL_JNDI_PATH = "arin.template.processor.regrws_url";

    /**
     * Lookup the Reg-RWS URL from JNDI. The RestEasy client uses this address.
     *
     * @return the Reg-RWS base URL
     */
    public static String getClientBaseUrl()
    {
        return SystemPropertiesHelper.getProperty( BASE_URL_JNDI_PATH );
    }

    public static PocService getPocService()
    {
        ClientExecutor executor = new ApacheHttpClient4Executor( ClientFactory.createHttpClient() );
        return ProxyFactory.create( PocService.class, getClientBaseUrl(), new LoggingExecutor( executor ) );
    }

    public static OrgService getOrgService()
    {
        ClientExecutor executor = new ApacheHttpClient4Executor( ClientFactory.createHttpClient() );
        return ProxyFactory.create( OrgService.class, getClientBaseUrl(), new LoggingExecutor( executor ) );
    }

    public static NetService getNetService()
    {
        ClientExecutor executor = new ApacheHttpClient4Executor( ClientFactory.createHttpClient() );
        return ProxyFactory.create( NetService.class, getClientBaseUrl(), new LoggingExecutor( executor ) );
    }

    public static CustomerService getCustomerService()
    {
        ClientExecutor executor = new ApacheHttpClient4Executor( ClientFactory.createHttpClient() );
        return ProxyFactory.create( CustomerService.class, getClientBaseUrl(), new LoggingExecutor( executor ) );
    }
}
