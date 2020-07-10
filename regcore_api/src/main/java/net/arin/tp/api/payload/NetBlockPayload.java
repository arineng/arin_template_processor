package net.arin.tp.api.payload;

import net.arin.tp.api.utils.IPUtils;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Comparator;

/**
 * <p>
 * The NetBlockPayload structure provides access to net block related data on the Network. The
 * <code>NetBlockPayload</code> is only returned as a part of the {@link NetPayload NetPayload}. See
 * {@link NetPayload NetPayload} for additional details.
 * </p>
 *
 * <p>
 * The description will be determined by the type. You do not need to set the description on the payload. If you do, it
 * will be discarded.
 * </p>
 */
@XmlRootElement( name = "netBlock" )
@XmlAccessorType( XmlAccessType.NONE )
public class NetBlockPayload implements Payload, Comparable<NetBlockPayload>
{
    static final public String FIELD_START_ADDRESS = "startAddress";
    static final public String FIELD_END_ADDRESS = "endAddress";
    static final public String FIELD_CIDR_LENGTH = "cidrLength";
    static final public String FIELD_DESCRIPTION = "description";
    static final public String FIELD_TYPE = "type";

    private String startAddress;
    private String endAddress;
    private String cidrLength;
    private NetType type;

    /**
     * The <code>NetType</code> allows you to specify the type of the network.
     */
    public enum NetType
    {
        /**
         * Used to represent a Reallocation network.
         */
        A( "Reallocated" ),

        /**
         * Used to represent an AFRINIC allocated network.
         */
        AF( "AFRINIC Allocated" ),

        /**
         * Used to represent an APNIC allocated network.
         */
        AP( "APNIC Allocated" ),

        /**
         * Used to represent an ARIN allocated network.
         */
        AR( "ARIN Allocated" ),

        /**
         * Used to represent an ARIN early reservation network.
         */
        AV( "ARIN Early Reservation" ),

        /**
         * Used to represent a Direct Allocation network.
         */
        DA( "Direct Allocation" ),

        /**
         * Used to represent a Direct Assignment network.
         */
        DS( "Direct Assignment" ),

        /**
         * Used to represent an AFRINIC transferred network.
         */
        FX( "AFRINIC Transferred" ),

        /**
         * Used to represent an IANA reserved network.
         */
        IR( "IANA Reserved" ),

        /**
         * Used to represent an IANA special use network.
         */
        IU( "IANA Special Use" ),

        /**
         * Used to represent a LACNIC allocated network.
         */
        LN( "LACNIC Allocated" ),

        /**
         * Used to represent a LACNIC transferred network.
         */
        LX( "LACNIC Transferred" ),

        /**
         * Used to represent an APNIC early reservation network.
         */
        PV( "APNIC Early Reservation" ),

        /**
         * Used to represent an APNIC early registration network.
         */
        PX( "APNIC Early Registration" ),

        /**
         * Used to represent a RIPE NCC allocated network.
         */
        RD( "RIPE NCC Allocated" ),

        /**
         * Used to represent a RIPE allocated network.
         */
        RN( "RIPE Allocated" ),

        /**
         * Used to represent a RIPE early reservation network.
         */
        RV( "RIPE Early Reservation" ),

        /**
         * Used to represent a RIPE NCC Transferred network.
         */
        RX( "RIP NCC Transferred" ),

        /**
         * Used to represent a Reassigned network.
         */
        S( "Reassigned" );

        // TOP LEVEL NETS
        // IANA blocks that have not been allocated, IU can be under an AV network.
        public static final NetType IANA_SPECIAL_USE = IU;
        public static final NetType IANA_RESERVED = IR;

        // Allocations by IANA to RIRs.
        public static final NetType AFRNIC_ALLOCATED = AF;
        public static final NetType APNIC_ALLOCATED = AP;
        public static final NetType ARIN_ALLOCATED = AR;
        public static final NetType LACNIC_ALLOCATED = LN;
        public static final NetType RIPE_ALLOCATED = RN;
        public static final NetType RIPE_NCC_ALLOCATED = RD;

        // Early allocations by IANA pre-RIRs.
        public static final NetType APNIC_EARLY_RESERVATION = PV;
        public static final NetType ARIN_EARLY_RESERVATION = AV;
        public static final NetType RIPE_EARLY_RESERVATION = RV;

        // LEVEL 2 NETS OUT OF AR/AV BLOCKS
        // Early exchange/shared (ERX) networks - these are managed by another RIR.
        public static final NetType LACNIC_TRANSFERRED = LX;
        public static final NetType APNIC_EARLY_REGISTRATION = PX;
        public static final NetType RIPE_NCC_TRANSFERRED = RX;
        public static final NetType AFRNIC_TRANSFERRED = FX;

        // ARIN DIRECT NETS
        // Parent net type can be null, AR, AV, RV, PV, LN or AF.
        public static final NetType DIRECT_ALLOCATION = DA;
        public static final NetType DIRECT_ASSIGNMENT = DS;

        // LEVEL 3+ NETS OUT OF DA NETS ONLY
        public static final NetType REALLOCATED = A; // Can be hierarchical.
        public static final NetType REASSIGNED = S; // Can NOT have children.

        private String description;

        NetType( String description )
        {
            this.description = description;
        }

        public String getDescription()
        {
            return description;
        }
    }

    @XmlElement( name = FIELD_START_ADDRESS )
    public String getStartAddress()
    {
        return startAddress;
    }

    public void setStartAddress( String startAddress )
    {
        this.startAddress = startAddress;
    }

    @XmlElement( name = FIELD_END_ADDRESS )
    public String getEndAddress()
    {
        return endAddress;
    }

    public void setEndAddress( String endAddress )
    {
        this.endAddress = endAddress;
    }

    @XmlElement( name = FIELD_CIDR_LENGTH )
    public String getCidrLength()
    {
        return cidrLength;
    }

    public void setCidrLength( String cidrLength )
    {
        this.cidrLength = cidrLength;
    }

    @XmlElement( name = FIELD_TYPE )
    public NetType getType()
    {
        return type;
    }

    public void setType( NetType type )
    {
        this.type = type;
    }

    @XmlElement( name = FIELD_DESCRIPTION )
    public String getDescription()
    {
        return ( type != null ? type.getDescription() : null );
    }

    public void setDescription( String description )
    {
        // Do nothing. We derive this from the type.
    }

    @Override
    public boolean equals( Object anotherNetBlock )
    {
        if ( !( anotherNetBlock instanceof NetBlockPayload ) )
        {
            return false;
        }

        NetBlockPayload toCompare = ( NetBlockPayload ) anotherNetBlock;

        return IPUtils.equals( this.startAddress, toCompare.startAddress )
                && IPUtils.equals( this.endAddress, toCompare.endAddress )
                && StringUtils.equals( this.cidrLength, toCompare.cidrLength )
                && this.type == toCompare.type;
    }

    public int compareTo( NetBlockPayload netBlockPayload )
    {
        NetBlockPayloadComparator comparator = new NetBlockPayloadComparator();

        return comparator.compare( this, netBlockPayload );
    }

    @Override
    public int hashCode()
    {
        int result = 17;

        result = 37 * result
                + ( startAddress != null ? startAddress : "" ).hashCode();
        result = 37 * result
                + ( ( endAddress != null ? endAddress : "" ).hashCode() );
        result = 37 * result
                + ( ( cidrLength != null ? cidrLength : "" ).hashCode() );
        result = 37 * result
                + ( ( type != null ) ? type.name() : "" ).hashCode();

        return result;
    }

    static class NetBlockPayloadComparator implements Comparator<NetBlockPayload>
    {
        public int compare( NetBlockPayload payload, NetBlockPayload anotherPayload )
        {
            String payloadStr = StringUtils.trimToEmpty( payload.getStartAddress() ) +
                    StringUtils.trimToEmpty( payload.getEndAddress() ) +
                    StringUtils.trimToEmpty( payload.getCidrLength() ) +
                    ( ( null == payload.getType() ) ? "" : payload.getType().name() );

            String anotherPayloadStr = StringUtils.trimToEmpty( anotherPayload.getStartAddress() ) +
                    StringUtils.trimToEmpty( anotherPayload.getEndAddress() ) +
                    StringUtils.trimToEmpty( anotherPayload.getCidrLength() ) +
                    ( ( null == payload.getType() ) ? "" : anotherPayload.getType().name() );

            return payloadStr.compareTo( anotherPayloadStr );
        }
    }
}
