package net.arin.tp.processor.response;

import net.arin.tp.api.payload.OrgPayload;
import net.arin.tp.api.payload.TicketPayload;
import org.apache.velocity.VelocityContext;

public class TicketedReallocateResponseContext extends TicketedResponseContext
{
    private TicketPayload ticketPayload;
    private OrgPayload orgPayload;

    TicketedReallocateResponseContext( TicketPayload ticketPayload, OrgPayload orgPayload )
    {
        this.ticketPayload = ticketPayload;
        this.orgPayload = orgPayload;
    }

    @Override
    public void setup( VelocityContext context )
    {
        super.setup( context );

        context.put( "ticketPayload", ticketPayload );
        context.put( "orgPayload", orgPayload );
    }

    @Override
    public String getTemplate()
    {
        return "email/reallocateTicket.vm";
    }

    @Override
    public ResponseType getResponseType()
    {
        return ResponseType.APPROVED;
    }
}
