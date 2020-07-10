package net.arin.tp.api.payload;

import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Comparator;

/**
 * This represents a poc link.
 * <p/>
 * The description will be determined by the function. You do not need to set the description on the payload. If you do,
 * it will be discarded.
 * <p/>
 * Note that the Admin ("AD") function is not applicable to network resources.
 */
@XmlRootElement( name = "pocLinkRef" )
@XmlAccessorType( XmlAccessType.NONE )
public class PocLinkPayload implements Payload
{
    static final public String ATTRIBUTE_HANDLE = "handle";
    static final public String ATTRIBUTE_FUNCTION = "function";
    static final public String ATTRIBUTE_DESCRIPTION = "description";

    private String handle;
    private PocLinkPayload.Function function;

    /**
     * The <code>Function</code> allows you to specify the function of the related POC.
     */
    public enum Function
    {
        /**
         * Used to represent an Admin POC.
         */
        AD( "Admin" ),

        /**
         * Used to represent an Abuse POC.
         */
        AB( "Abuse" ),

        /**
         * Used to represent a NOC POC.
         */
        N( "NOC" ),

        /**
         * Used to represent a Tech POC.
         */
        T( "Tech" ),

        /**
         * Used to represent a Routing POC.
         */
        R( "Routing" ),

        /**
         * Used to represent a DNS POC.
         */
        D( "DNS" );

        private String description;

        Function( String description )
        {
            this.description = description;
        }

        public String getDescription()
        {
            return description;
        }
    }

    /**
     * No arg constructor required for JAXB marshalling.
     */
    public PocLinkPayload()
    {
    }

    public PocLinkPayload( String handle, Function function )
    {
        this.handle = handle;
        this.function = function;
    }

    @XmlAttribute( name = ATTRIBUTE_HANDLE )
    public String getHandle()
    {
        return this.handle;
    }

    public void setHandle( String handle )
    {
        this.handle = handle;
    }

    @XmlAttribute( name = ATTRIBUTE_FUNCTION )
    public Function getFunction()
    {
        return this.function;
    }

    public void setFunction( Function function )
    {
        this.function = function;
    }

    @XmlAttribute( name = ATTRIBUTE_DESCRIPTION )
    public String getDescription()
    {
        return getFunction().getDescription();
    }

    /**
     * This method will not actually set the description. The description is dictated by the function alone and
     * therefore cannot be set by a user.
     */
    public void setDescription( String description )
    {
        // Discard. We get this from the function description.
    }

    @Override
    public boolean equals( Object toCompare )
    {
        if ( !( toCompare instanceof PocLinkPayload ) )
        {
            return false;
        }

        PocLinkPayload payload = ( PocLinkPayload ) toCompare;
        return ( this.function == payload.getFunction() ) && ( StringUtils.equalsIgnoreCase(
                StringUtils.trim( this.handle ), StringUtils.trim( payload.getHandle() ) ) );
    }

    @Override
    public int hashCode()
    {
        return ( this.handle + this.function.name() ).hashCode();
    }

    static class PocLinkPayloadComparator implements Comparator<PocLinkPayload>
    {
        public int compare( PocLinkPayload payload, PocLinkPayload anotherPayload )
        {
            String payloadStr = payload.getFunction().name() +
                    StringUtils.trimToEmpty( payload.getHandle().toLowerCase() );

            String anotherPayloadStr = anotherPayload.getFunction().name() +
                    StringUtils.trimToEmpty( anotherPayload.getHandle().toLowerCase() );

            return payloadStr.compareTo( anotherPayloadStr );
        }
    }
}
