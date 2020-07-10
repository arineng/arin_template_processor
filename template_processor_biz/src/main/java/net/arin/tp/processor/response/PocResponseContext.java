package net.arin.tp.processor.response;

import net.arin.tp.api.payload.PocPayload;
import org.apache.velocity.VelocityContext;

public class PocResponseContext extends AbstractResponseContext
{
    private PocPayload pocPayload;

    PocResponseContext( PocPayload pocPayload )
    {
        this.pocPayload = pocPayload;
    }

    @Override
    public void setup( VelocityContext context )
    {
        context.put( "pocPayload", pocPayload );
    }

    @Override
    public String getTemplate()
    {
        return "email/pocSuccessful.vm";
    }

    @Override
    public ResponseType getResponseType()
    {
        return ResponseType.APPROVED;
    }
}
