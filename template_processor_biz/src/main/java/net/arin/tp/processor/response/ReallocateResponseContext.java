package net.arin.tp.processor.response;

import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.OrgPayload;
import org.apache.velocity.VelocityContext;

public class ReallocateResponseContext extends AbstractResponseContext
{
    private NetPayload netPayload;
    private OrgPayload orgPayload;

    ReallocateResponseContext( NetPayload netPayload, OrgPayload orgPayload )
    {
        this.netPayload = netPayload;
        this.orgPayload = orgPayload;
    }

    @Override
    public void setup( VelocityContext context )
    {
        context.put( "netPayload", netPayload );
        context.put( "orgPayload", orgPayload );
    }

    @Override
    public String getTemplate()
    {
        return "email/reallocateSuccessful.vm";
    }

    @Override
    public ResponseType getResponseType()
    {
        return ResponseType.APPROVED;
    }
}
