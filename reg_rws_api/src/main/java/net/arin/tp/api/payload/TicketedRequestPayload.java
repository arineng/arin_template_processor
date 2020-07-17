package net.arin.tp.api.payload;

import net.arin.tp.api.annotations.AutonomousElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * The TicketedRequestPayload structure provides access to either a net or a ticket. In the event a service call is
 * made that can result in a network being returned or a ticket being returned, a <code>TicketedRequestPayload</code>
 * is returned. Only one of these elements will be present.
 * </p>
 *
 * <p>
 * Currently, the only instance in which this occurs is a SWIP. If your reallocation or reassignment meets the
 * conditions for auto processing, the TicketedRequest payload will have an embedded NetPayload representing the net
 * that was created as a result of your request. If your reallocation or reassignment does not meet the conditions for
 * auto processing, the TicketRequestPayload will have an embedded TicketPayload representing the ticket that was
 * created for your request. See the
 * {@link net.arin.tp.api.service.NetService#reassign(String, String, NetPayload) reassign} and
 * {@link net.arin.tp.api.service.NetService#reallocate(String, String, NetPayload) reallocate} for more details.
 * </p>
 */
@XmlRootElement( name = "ticketedRequest" )
@XmlAccessorType( XmlAccessType.NONE )
public class TicketedRequestPayload implements Payload
{
    private TicketPayload ticket;
    private NetPayload net;

    @XmlElement( name = "ticket" )
    @AutonomousElement
    public TicketPayload getTicket()
    {
        return ticket;
    }

    public void setTicket( TicketPayload ticket )
    {
        this.ticket = ticket;
    }

    @XmlElement( name = "net" )
    @AutonomousElement
    public NetPayload getNet()
    {
        return net;
    }

    public void setNet( NetPayload net )
    {
        this.net = net;
    }
}
