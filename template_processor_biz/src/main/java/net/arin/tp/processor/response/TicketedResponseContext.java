package net.arin.tp.processor.response;

import net.arin.tp.utils.SystemPropertiesHelper;
import org.apache.velocity.VelocityContext;

public abstract class TicketedResponseContext extends AbstractResponseContext
{
    private static final SystemPropertiesHelper properties = new SystemPropertiesHelper();

    public boolean isTicketed()
    {
        return true;
    }

    @Override
    public void setup( VelocityContext context )
    {
        context.put( "publicBaseUrl", properties.lookupString( "arin.template.processor.baseurl_public" ) );
    }
}
