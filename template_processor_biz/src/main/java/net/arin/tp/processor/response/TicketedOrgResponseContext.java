package net.arin.tp.processor.response;

import net.arin.tp.api.payload.TicketPayload;
import org.apache.velocity.VelocityContext;

public class TicketedOrgResponseContext extends TicketedResponseContext
{
    private TicketPayload ticketPayload;

    TicketedOrgResponseContext( TicketPayload ticketPayload )
    {
        this.ticketPayload = ticketPayload;
    }

    @Override
    public void setup( VelocityContext context )
    {
        super.setup( context );
        context.put( "ticketPayload", ticketPayload );
    }

    @Override
    public String getTemplate()
    {
        return "email/createOrgSuccessful.vm";
    }

    @Override
    public ResponseType getResponseType()
    {
        return ResponseType.APPROVED;
    }
}
