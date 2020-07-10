package net.arin.tp.api.payload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The DelegationPayload structure provides access to the data available for network delegations.
 * <p/>
 * The following field is system generated:
 * <ul>
 * <li>name</li>
 * </ul>
 * This field will be set by our registration system and may not be set or modified by you.
 * <p/>
 * When performing a modify, if you include this field with a different value from the original, omit it entirely, or
 * leave it blank, it will result in an error. It is recommended you perform a fresh GET prior to any modification and
 * make your changes on the payload returned. This will ensure you have the latest system generated values.
 * <p>
 * You are able to specify the <strong>Time To Live (TTL)</strong> if you use the namespace
 * http://www.arin.net/regrws/ttl/v1. The example below demonstrates how to use the namespace to modify a TTL for both a
 * Delegation Key and a Nameserver. If the TTL is omitted entirely, the current TTL will be used. If there is no current
 * TTL, or if the TTL is an empty string, a default value will be used.
 * </p>
 * <p>
 * <h3>Delegation Payload when specifying TTLs</h3>
 * <pre>
 * &lt;delegation xmlns="http://www.arin.net/regrws/core/v1" xmlns:ns2="http://www.arin.net/regrws/ttl/v1"&gt;
 * &lt;name&gt;0.d.5.f.7.0.6.2.ip6.arpa.&lt;/name&gt;
 * &lt;delegationKeys&gt;
 *   &lt;delegationKey&gt;
 *     &lt;algorithm name="RSA/SHA-1"&gt;5&lt;/algorithm&gt;
 *     &lt;digest&gt;0DC99D4B6549F83385214189CA48DC6B209ABB71&lt;/digest&gt;
 *     &lt;digestType name="SHA-1"&gt;1&lt;/digestType&gt;
 *     &lt;keyTag&gt;264&lt;/keyTag&gt;
 *     &lt;ns2:ttl&gt;86400&lt;/ns2:ttl&gt;
 *   &lt;/delegationKey&gt;
 * &lt;/delegationKeys&gt;
 *   &lt;nameservers&gt;
 *     &lt;nameserver ns2:ttl="10800"&gt;NS0.DOMAIN.COM&lt;/nameserver&gt;
 *     &lt;nameserver&gt;NS1.DOMAIN.COM&lt;/nameserver&gt;
 *   &lt;/nameservers&gt;
 * &lt;/delegation&gt;
 * </pre>
 * </p>
 */
@XmlRootElement( name = "delegation" )
@XmlAccessorType( XmlAccessType.NONE )
public class DelegationPayload implements Payload
{
    static final public String FIELD_NAME = "name";
    static final public String WRAP_DELEGATION_KEYS = "delegationKeys";
    static final public String WRAP_NAMESERVER = "nameservers";
    static final public String FIELD_NAMESERVER = "nameserver";
    static final public String FIELD_TTL = "ttl";
    static final public String WRAP_KEY = "delegationKeys";
    static final public String FIELD_KEY = "delegationKey";

    static final public String NAMESPACE2 = "http://www.arin.net/regrws/ttl/v1";

    private String name;
    private List<NameserverPayload> nameservers;
    private List<DelegationKeyPayload> keys;

    public DelegationPayload()
    {
        nameservers = new ArrayList<>();
        keys = new ArrayList<>();
    }

    @XmlElement( name = FIELD_NAME )
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @XmlElementWrapper( name = WRAP_NAMESERVER )
    @XmlElement( name = FIELD_NAMESERVER, type = NameserverPayload.class )
    public List<NameserverPayload> getNameservers()
    {
        return this.nameservers;
    }

    public void setNameservers( List<NameserverPayload> nameservers )
    {
        this.nameservers = nameservers;
    }

    @XmlElement( name = FIELD_KEY )
    @XmlElementWrapper( name = WRAP_KEY )
    public List<DelegationKeyPayload> getKeys()
    {
        return keys;
    }

    public void setKeys( List<DelegationKeyPayload> keys )
    {
        this.keys = keys;
    }
}
