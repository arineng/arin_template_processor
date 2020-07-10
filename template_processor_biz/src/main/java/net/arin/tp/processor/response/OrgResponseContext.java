package net.arin.tp.processor.response;

import net.arin.tp.api.payload.OrgPayload;
import org.apache.velocity.VelocityContext;

public class OrgResponseContext extends AbstractResponseContext
{
    private OrgPayload orgPayload;

    OrgResponseContext( OrgPayload orgPayload )
    {
        this.orgPayload = orgPayload;
    }

    @Override
    public void setup( VelocityContext context )
    {
        context.put( "orgPayload", orgPayload );
    }

    @Override
    public String getTemplate()
    {
        return "email/orgSuccessful.vm";
    }

    @Override
    public ResponseType getResponseType()
    {
        return ResponseType.APPROVED;
    }
}
