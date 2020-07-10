package net.arin.tp.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is intended to be used alongside an @XmlElement annotation. When documentation is generated, it may
 * be desirable to generate an example payload for one of a couple of specific properties. This class is being created
 * to handle the current situation with the TicketedRequestPayload.
 * <p/>
 * A specific example:
 * <p/>
 * TicketedRequestPayload is a wrapper element that has both a getNet() and getTicket() method to obtain the contained
 * NetPayload or TicketPayload respectively. However, both values will never be present - only one or the other. The
 * documentation should be generated to represent one payload that is a ticketed request that contains a NetPayload and
 * another that contains a TicketPayload.
 */
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface AutonomousElement
{
}
