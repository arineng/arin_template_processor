package net.arin.tp.ipaddr;

import java.math.BigInteger;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

/**
 * Used to store an IP address range. This differs from {@link CIDR} since it can represent off-bit ranges.
 */
public class IPRange implements Comparable<IPRange>, IsIPVersionAware
{
    private IPAddr startAddress;
    private IPAddr endAddress;
    private BigInteger numberOfIPAddressesInRange = BigInteger.ZERO;
    private List<CIDR> cidrs;

    public IPRange( IPAddr startAddress, IPAddr endAddress )
    {
        initialize( startAddress, endAddress );
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        IPRange ipRange = ( IPRange ) o;

        if ( !Objects.equals( endAddress, ipRange.endAddress ) )
        {
            return false;
        }
        return Objects.equals( startAddress, ipRange.startAddress );
    }

    @Override
    public int hashCode()
    {
        int result = startAddress != null ? startAddress.hashCode() : 0;
        result = 31 * result + ( endAddress != null ? endAddress.hashCode() : 0 );
        return result;
    }

    public IPRange( String startAddress, String endAddress )
    {
        this( new IPAddr( startAddress ), new IPAddr( endAddress ) );
    }

    public IPRange( IPAddr startAddress, int cidrLength )
    {
        try
        {
            CIDR cidr = CIDR.findContainingCIDR( startAddress, cidrLength );
            if ( cidr.getPrefix().equals( startAddress ) )
            {
                initialize( startAddress, cidr.getEndAddress() );
            }
            else
            {
                throw new IllegalArgumentException( "The start IP address is off-bit for the given CIDR length." );
            }
        }
        catch ( UnknownHostException e )
        {
            throw new IllegalArgumentException( e.getMessage(), e );
        }
    }

    private void initialize( IPAddr startAddress, IPAddr endAddress )
    {
        // CIDR.calcCIDRs() makes sure that start and end are the same ip version, and that start < end.
        cidrs = CIDR.calcCIDRs( startAddress, endAddress );

        this.startAddress = startAddress;
        this.endAddress = endAddress;

        for ( CIDR cidr : cidrs )
        {
            numberOfIPAddressesInRange = numberOfIPAddressesInRange.add( CIDR.ipPrefixLengthToLength( cidr ) );
        }
    }

    public boolean isOffBit()
    {
        return cidrs.size() != 1;
    }

    public IPAddr getStartIPAddress()
    {
        return startAddress;
    }

    public IPAddr getEndIPAddress()
    {
        return endAddress;
    }

    /**
     * Get the total number of addresses in the IP Range.
     *
     * @return total number of addresses in range
     */
    public BigInteger getNumberOfIPAddressesInRange()
    {
        return numberOfIPAddressesInRange;
    }

    /**
     * Get the CIDR representation of the IP Range.
     *
     * @return List of CIDRs
     */
    public List<CIDR> getCidrs()
    {
        return cidrs;
    }

    @Override
    public String toString()
    {
        return String.format( "%s-%s", startAddress, endAddress );
    }

    public int compareTo( IPRange range )
    {
        int r = getStartIPAddress().compareTo( range.getStartIPAddress() );
        if ( r != 0 )
        {
            return r;
        }
        return range.getEndIPAddress().compareTo( getEndIPAddress() );
    }

    public String getFormattedAddresses()
    {
        DecimalFormat df = new DecimalFormat();
        df.getDecimalFormatSymbols().setGroupingSeparator( ',' );
        return df.format( numberOfIPAddressesInRange );
    }

    public boolean isV4()
    {
        return getVersion() == IPVersion.IPV4;
    }

    public boolean isV6()
    {
        return getVersion() == IPVersion.IPV6;
    }

    public IPVersion getVersion()
    {
        return getStartIPAddress().getVersion();
    }
}
