package net.arin.tp.api.payload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>
 * The NameserverPayload structure provides access to the Nameserver data available via ARIN Online.
 * </p>
 *
 * <p>
 * To specify a <strong>Time to Live (TTL)</strong>, you will need to add the TTL namespace, which is
 * http://www.arin.net/regrws/ttl/v1. The example below demonstrates how to use the namespace to modify a TTL. If the
 * TTL is omitted entirely, the current TTL will be used. If there is no current TTL, or if the TTL is an empty string,
 * a default value will be used.
 * </p>
 *
 * <p>
 * <h3>Nameserver Payload with TTL</h3>
 * <pre>
 * &lt;nameserver xmlns="http://www.arin.net/regrws/core/v1" xmlns:ns2="http://www.arin.net/regrws/ttl/v1" ns2:ttl=86401&gt;NS3.DOMAIN.COM&lt;/nameserver&gt;
 * </pre>
 * </p>
 */
@XmlRootElement( name = "nameserver" )
@XmlAccessorType( XmlAccessType.NONE )
public class NameserverPayload implements Payload
{
    private String nameserver;
    private String ttl;

    private static final String FIELD_TTL = "ttl";

    public NameserverPayload( String nameserver, String ttl )
    {
        this.nameserver = nameserver;
        this.ttl = ttl;
    }

    public NameserverPayload()
    {
    }

    @XmlAttribute( name = FIELD_TTL, namespace = DelegationPayload.NAMESPACE2 )
    public String getTtl()
    {
        return this.ttl;
    }

    public void setTtl( String ttl )
    {
        this.ttl = ttl;
    }

    @XmlValue
    public String getNameserver()
    {
        return this.nameserver;
    }

    public void setNameserver( String nameserver )
    {
        this.nameserver = nameserver;
    }
}
