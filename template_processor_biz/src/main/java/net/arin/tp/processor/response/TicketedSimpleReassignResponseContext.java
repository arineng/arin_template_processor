package net.arin.tp.processor.response;

import net.arin.tp.api.payload.CustomerPayload;
import net.arin.tp.api.payload.TicketPayload;
import org.apache.velocity.VelocityContext;

public class TicketedSimpleReassignResponseContext extends TicketedResponseContext
{
    private TicketPayload ticketPayload;
    private CustomerPayload customerPayload;

    TicketedSimpleReassignResponseContext( TicketPayload ticketPayload, CustomerPayload customerPayload )
    {
        this.ticketPayload = ticketPayload;
        this.customerPayload = customerPayload;
    }

    @Override
    public void setup( VelocityContext context )
    {
        super.setup( context );

        context.put( "ticketPayload", ticketPayload );
        context.put( "customerPayload", customerPayload );
    }

    @Override
    public String getTemplate()
    {
        return "email/simpleReassignTicket.vm";
    }

    @Override
    public ResponseType getResponseType()
    {
        return ResponseType.APPROVED;
    }
}
