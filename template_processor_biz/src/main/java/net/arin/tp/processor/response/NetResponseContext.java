package net.arin.tp.processor.response;

import net.arin.tp.api.payload.NetPayload;
import org.apache.velocity.VelocityContext;

/**
 * Sets the necessary values in the Velocity context used to render an "net action successful" email template.
 */
public class NetResponseContext extends AbstractResponseContext
{
    private NetPayload netPayload;

    NetResponseContext( NetPayload netPayload )
    {
        this.netPayload = netPayload;
    }

    @Override
    public void setup( VelocityContext context )
    {
        context.put( "netPayload", netPayload );
    }

    @Override
    public String getTemplate()
    {
        return "email/netSuccessful.vm";
    }

    @Override
    public ResponseType getResponseType()
    {
        return ResponseType.APPROVED;
    }
}
