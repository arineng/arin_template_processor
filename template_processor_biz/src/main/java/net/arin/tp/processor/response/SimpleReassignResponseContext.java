package net.arin.tp.processor.response;

import net.arin.tp.api.payload.CustomerPayload;
import net.arin.tp.api.payload.NetPayload;
import org.apache.velocity.VelocityContext;

public class SimpleReassignResponseContext extends AbstractResponseContext
{
    private NetPayload netPayload;
    private CustomerPayload customerPayload;

    SimpleReassignResponseContext( NetPayload netPayload, CustomerPayload customerPayload )
    {
        this.netPayload = netPayload;
        this.customerPayload = customerPayload;
    }

    @Override
    public void setup( VelocityContext context )
    {
        context.put( "netPayload", netPayload );
        context.put( "customerPayload", customerPayload );
    }

    @Override
    public String getTemplate()
    {
        return "email/simpleReassignSuccessful.vm";
    }

    @Override
    public ResponseType getResponseType()
    {
        return ResponseType.APPROVED;
    }
}
