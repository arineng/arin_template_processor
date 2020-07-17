package net.arin.tp.api.service;

import net.arin.tp.api.BaseTest;
import org.testng.annotations.Test;

import javax.ws.rs.DELETE;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class ServiceBaseTest extends BaseTest
{
    protected abstract Class getServiceClass();

    /*
     * If you send a payload to a DELETE service, RESTeasy will burp up smelly exceptions from deep within itself.
     * (see org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor.loadHttpMethod for the gory details)
     * Interestingly enough, the HTTP spec is silent on the issue.
     */
    @Test
    public void testDeleteAnnotatedServicesDontHavePayloads() throws Exception
    {
        for ( Method method : getServiceClass().getMethods() )
        {
            for ( Annotation annotation : method.getAnnotations() )
            {
                if ( annotation instanceof DELETE )
                {
                    for ( Annotation[] annotations : method.getParameterAnnotations() )
                    {
                        // If we have a parameter with no annotations.
                        if ( annotations.length == 0 )
                        {
                            throw new Exception(
                                    getServiceClass().getName() + "::" + method.getName() + ": " +
                                            "@DELETE annotated methods can't take payloads or RESTeasy will be angry."
                            );
                        }
                    }
                }
            }
        }
    }
}
