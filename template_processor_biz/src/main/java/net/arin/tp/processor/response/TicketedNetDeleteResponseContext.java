package net.arin.tp.processor.response;

import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.TicketPayload;
import org.apache.velocity.VelocityContext;

public class TicketedNetDeleteResponseContext extends TicketedResponseContext
{
    private TicketPayload ticketPayload;
    private NetPayload netPayload;

    TicketedNetDeleteResponseContext( TicketPayload ticketPayload, NetPayload netPayload )
    {
        this.ticketPayload = ticketPayload;
        this.netPayload = netPayload;
    }

    @Override
    public void setup( VelocityContext context )
    {
        super.setup( context );

        context.put( "netPayload", netPayload );
        context.put( "ticketPayload", ticketPayload );
    }

    @Override
    public String getTemplate()
    {
        return "email/netDeleteTicket.vm";
    }

    @Override
    public ResponseType getResponseType()
    {
        return ResponseType.APPROVED;
    }
}
