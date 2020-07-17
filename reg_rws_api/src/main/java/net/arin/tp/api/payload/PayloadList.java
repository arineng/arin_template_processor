package net.arin.tp.api.payload;

import net.arin.tp.api.annotations.InternalListTypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * This payload is used as a container, to store multiple payloads and return them back to the customer. This list
 * payload will act as a wrapper for net searching, the setting of phones on POCs, etc.
 */
@XmlRootElement( name = "collection" )
@XmlAccessorType( XmlAccessType.NONE )
public class PayloadList<T extends Payload>
{
    private List<T> payloads;

    public PayloadList()
    {
        payloads = new ArrayList<>();
    }

    public PayloadList( List<T> payloads )
    {
        this.payloads = payloads;
    }

    @XmlElements( {
            @XmlElement( name = "phone", type = PhonePayload.class ),
            @XmlElement( name = "net", type = NetPayload.class )
    } )
    @InternalListTypes( { NetPayload.class } )
    public List<T> getPayloads()
    {
        return payloads;
    }
}
