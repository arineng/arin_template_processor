package net.arin.tp.api.payload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>
 * The DelegationKeyPayload structure provides access to the key data available via ARIN Online.
 * <p/>
 * The algorithm name and digest type name values will be determined by the element value. You do not need to set the
 * name on the payload. If you do, it will be discarded.
 * <p/>
 * The valid values for the <strong>algorithm</strong> element are:
 * <ul>
 * <li>5</li>
 * <li>7</li>
 * <li>8</li>
 * <li>10</li>
 * </ul>
 * <p/>
 * The valid values for the <strong>digest type</strong> element are:
 * <ul>
 * <li>1</li>
 * <li>2</li>
 * <li>3</li>
 * </ul>
 * </p>
 * <p>
 * To specify a <strong>Time to Live (TTL)</strong>, you will need to add the TTL namespace, which is
 * http://www.arin.net/regrws/ttl/v1. The example below demonstrates how to use the namespace to modify a TTL. If the
 * TTL is omitted entirely, the current TTL will be used. If there is no current TTL, or if the TTL element is empty, a
 * default value will be used.
 * </p>
 * <p>
 * <h3>DelegationKey Payload when specifying TTLs</h3>
 * <pre>
 *   &lt;delegationKey xmlns="http://www.arin.net/regrws/core/v1" xmlns:ns2="http://www.arin.net/regrws/ttl/v1" &gt;
 *     &lt;algorithm name="RSA/SHA-1"&gt;5&lt;/algorithm&gt;
 *     &lt;digest&gt;0DC99D4B6549F83385214189CA48DC6B209ABB71&lt;/digest&gt;
 *     &lt;digestType name="SHA-1"&gt;1&lt;/digestType&gt;
 *     &lt;keyTag&gt;264&lt;/keyTag&gt;
 *     &lt;ns2:ttl&gt;86400&lt;/ns2:ttl&gt;
 *   &lt;/delegationKey&gt;
 * </pre>
 * </p>
 */
@XmlRootElement( name = "delegationKey" )
@XmlAccessorType( XmlAccessType.NONE )
public class DelegationKeyPayload implements Payload
{
    private Algorithm algorithm;
    private String digest;
    private DigestType digestType;
    private String keyTag;
    private String ttl;

    public static final String FIELD_TTL = "ttl";
    public static final String FIELD_DELEGATION_KEY = "delegationKey";
    public static final String FIELD_DIGEST_TYPE = "digestType";
    public static final String FIELD_ALGORITHM = "algorithm";
    public static final String FIELD_KEYTAG = "keyTag";
    public static final String FIELD_DIGEST = "digest";
    public static final String ATTR_NAME = "name";

    @XmlElement( name = FIELD_TTL, namespace = DelegationPayload.NAMESPACE2 )
    public String getTtl()
    {
        return ttl;
    }

    public void setTtl( String ttl )
    {
        this.ttl = ttl;
    }

    @XmlElement( name = FIELD_ALGORITHM )
    public Algorithm getAlgorithm()
    {
        return algorithm;
    }

    public void setAlgorithm( Algorithm algorithm )
    {
        this.algorithm = algorithm;
    }

    @XmlElement( name = FIELD_DIGEST )
    public String getDigest()
    {
        return digest;
    }

    public void setDigest( String digest )
    {
        this.digest = digest;
    }

    @XmlElement( name = FIELD_DIGEST_TYPE )
    public DigestType getDigestType()
    {
        return digestType;
    }

    public void setDigestType( DigestType digestType )
    {
        this.digestType = digestType;
    }

    @XmlElement( name = FIELD_KEYTAG )
    public String getKeyTag()
    {
        return keyTag;
    }

    public void setKeyTag( String keyTag )
    {
        this.keyTag = keyTag;
    }

    @XmlAccessorType( XmlAccessType.NONE )
    public static class Algorithm
    {
        @XmlValue
        private String value;
        private String name;

        public String getValue()
        {
            return value;
        }

        public void setValue( String value )
        {
            this.value = value;
        }

        @XmlAttribute( name = ATTR_NAME )
        public String getName()
        {
            return name;
        }

        public void setName( String name )
        {
            this.name = name;
        }
    }

    @XmlAccessorType( XmlAccessType.NONE )
    public static class DigestType
    {
        @XmlValue
        private String value;
        private String name;

        public String getValue()
        {
            return value;
        }

        public void setValue( String value )
        {
            this.value = value;
        }

        @XmlAttribute( name = ATTR_NAME )
        public String getName()
        {
            return name;
        }

        public void setName( String name )
        {
            this.name = name;
        }
    }
}
