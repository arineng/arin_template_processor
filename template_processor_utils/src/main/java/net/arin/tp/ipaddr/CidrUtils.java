package net.arin.tp.ipaddr;

import org.apache.commons.lang.StringUtils;

import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CidrUtils
{
    // Used for base 2 math.
    public static final BigInteger TWO = BigInteger.valueOf( 2 );

    public static List<Integer> ipv4Num24BlocksToCidrPrefixes( BigInteger num24Blocks )
    {
        BigInteger size = num24Blocks.multiply( BigInteger.valueOf( 256 ) );
        return getOptimalCidrs( size, IPVersion.IPV4 );
    }

    public static BigInteger ipv4CidrsToNum24Blocks( List<CIDR> cidrs )
    {
        BigInteger result = BigInteger.ZERO;
        for ( CIDR cidr : cidrs )
        {
            int cidrPrefix = cidr.getPrefixLength();
            result = result.add( ipv4CidrPrefixToNum24Blocks( cidrPrefix ) );
        }
        return result;
    }

    public static BigInteger ipv4CidrsToNum32Blocks( List<CIDR> cidrs )
    {
        BigInteger result = BigInteger.ZERO;
        for ( CIDR cidr : cidrs )
        {
            int cidrPrefix = cidr.getPrefixLength();
            result = result.add( ipv4CidrPrefixToNum32Blocks( cidrPrefix ) );
        }
        return result;
    }

    public static BigInteger ipv4CidrPrefixesToNum24Blocks( List<Integer> cidrPrefixes )
    {
        BigInteger result = BigInteger.ZERO;
        for ( Integer cidrPrefix : cidrPrefixes )
        {
            result = result.add( ipv4CidrPrefixToNum24Blocks( cidrPrefix ) );
        }
        return result;
    }

    public static BigInteger ipv4CidrPrefixToNum24Blocks( int cidrPrefix )
    {
        return ipCidrPrefixToNumBlocks( 24, cidrPrefix, IPVersion.IPV4 );
    }

    public static BigInteger ipv4CidrPrefixToNum32Blocks( int cidrPrefix )
    {
        return ipCidrPrefixToNumBlocks( 32, cidrPrefix, IPVersion.IPV4 );
    }

    public static BigInteger ipv6CidrPrefixToNum128Blocks( int cidrPrefix )
    {
        return ipCidrPrefixToNumBlocks( 128, cidrPrefix, IPVersion.IPV6 );
    }

    public static BigInteger ipCidrPrefixToNumBlocks( int blockCidr, int cidrPrefix, IPVersion ipVersion )
    {
        if ( ( blockCidr - cidrPrefix ) < 0 )
        {
            throw new RuntimeException( "You cannot do that, blockCidr - cidrPrefix is less than zero" );
        }
        if ( IPVersion.IPV4 == ipVersion && ( blockCidr > 32 ) )
        {
            throw new RuntimeException( "You can't calculate blocks greater than /32 for V4" );
        }
        else if ( IPVersion.IPV6 == ipVersion && ( blockCidr > 128 ) )
        {
            throw new RuntimeException( "You can't calculate blockCidrs greater than /128 for v6 " );
        }

        return TWO.pow( blockCidr - cidrPrefix );
    }

    public static BigInteger ipCidrPrefixesToNumBlocks( int blockCidr, List<Integer> cidrPrefixes, IPVersion ipVersion )
    {
        BigInteger result = BigInteger.ZERO;
        for ( Integer cidrPrefix : cidrPrefixes )
        {
            result = result.add( ipCidrPrefixToNumBlocks( blockCidr, cidrPrefix, ipVersion ) );
        }
        return result;
    }

    public static BigInteger ipCidrsToNumBlocks( int blockCidr, List<CIDR> cidrs, IPVersion ipVersion )
    {
        BigInteger result = BigInteger.ZERO;

        for ( CIDR cidrPrefix : cidrs )
        {
            result = result.add( ipCidrPrefixToNumBlocks( blockCidr, cidrPrefix.getPrefixLength(), ipVersion ) );
        }

        return result;
    }

    /**
     * This method gets the optimal number of CIDR blocks for a block size for a given ip version.
     */
    public static List<Integer> getOptimalCidrs( BigInteger sizeWanted, IPVersion ipVersion )
    {
        List<Integer> cidrs = new ArrayList<>();
        int bitLength = sizeWanted.bitLength();
        int networkLength = ipVersion.getNumberOfBits();

        // To determine the optimal number of CIDR blocks just find all the bits that are turned on. Bits that were
        // trimmed (if bitLength < networkLength) were all zeros so we don't have to worry about them.
        for ( int i = bitLength - 1; i >= 0; i-- )
        {
            if ( sizeWanted.testBit( i ) )
            {
                cidrs.add( networkLength - i );
            }
        }
        return cidrs;
    }

    /**
     * Sort a group of CIDRs (V4 first, V6 after), and also remove any CIDRs that are contained by others. E.g.,
     * 173.238.0.0/16 will be removed if 173.238.0.0/15 present or another 173.238.0.0/16 present.
     */
    public static List<CIDR> sortAndMergeCidrs( List<CIDR> cidrs )
    {
        List<CIDR> v6Cidrs = new ArrayList<>();
        List<CIDR> v4Cidrs = new ArrayList<>();
        for ( CIDR cidr : cidrs )
        {
            if ( cidr.getPrefix().getVersion() == IPVersion.IPV6 )
            {
                v6Cidrs.add( cidr );
            }
            else
            {
                v4Cidrs.add( cidr );
            }
        }
        v6Cidrs = sortAndMergeCidrsOfSameVersion( v6Cidrs );
        v4Cidrs = sortAndMergeCidrsOfSameVersion( v4Cidrs );
        List<CIDR> results = new ArrayList<>();
        results.addAll( v6Cidrs );
        results.addAll( v4Cidrs );

        return results;
    }

    /**
     * Sort a group of CIDRs that have to be of same version, and also remove any CIDRs that are contained by others.
     * E.g., 173.238.0.0/16 will be removed if 173.238.0.0/15 present or another 173.238.0.0/16 present.
     */
    public static List<CIDR> sortAndMergeCidrsOfSameVersion( List<CIDR> cidrs )
    {
        if ( cidrs.isEmpty() )
        {
            return cidrs;
        }

        List<CIDR> sortedCidrs = new ArrayList<>( cidrs );
        sortedCidrs.sort( getComparatorForMerge() );

        List<CIDR> mergedCidrs = new ArrayList<>();
        for ( CIDR cidr : sortedCidrs )
        {
            if ( mergedCidrs.isEmpty() || !contains( mergedCidrs.get( mergedCidrs.size() - 1 ), cidr ) )
            {
                mergedCidrs.add( cidr );
            }
        }
        return mergedCidrs;
    }

    // We have to use a different comparator than the default to facilitate the merge. The CIDR default comparator makes
    // a sorted result: 173.238.0.0/16, 173.238.0.0/15, 173.239.0.0/16. But we actually want: 173.238.0.0/15,
    // 173.238.0.0/16, 173.239.0.0/16.
    protected static Comparator<CIDR> getComparatorForMerge()
    {
        return ( o1, o2 ) -> {
            int result = o1.getPrefix().compareTo( o2.getPrefix() );
            if ( result != 0 )
            {
                return result;
            }
            return o2.getEndAddress().compareTo( o1.getEndAddress() );
        };
    }

    protected static boolean contains( CIDR cidr1, CIDR cidr2 )
    {
        return cidr1.getPrefix().compareTo( cidr2.getPrefix() ) <= 0
                && cidr1.getEndAddress().compareTo( cidr2.getEndAddress() ) >= 0;
    }

    /**
     * Checks for overlapping CIDRs. For example, 10.0.0.0/16 and 10.0.0.0/32 overlap.
     *
     * @param cidr1 First CIDR
     * @param cidr2 Second CIDR
     * @return True if they overlap; false otherwise
     */
    public static boolean cidrsOverlap( CIDR cidr1, CIDR cidr2 )
    {
        if ( cidr1.equals( cidr2 ) )
        {
            return true;
        }

        if ( cidr1.getPrefix().compareTo( cidr2.getPrefix() ) <= 0 && cidr1.getEndAddress().compareTo( cidr2.getEndAddress() ) >= 0 )
        {
            return true;
        }

        return cidr1.getPrefix().compareTo( cidr2.getPrefix() ) >= 0 && cidr1.getEndAddress().compareTo( cidr2.getEndAddress() ) <= 0;
    }

    /**
     * Returns a CIDR object for a raw IP address string.
     *
     * @param rawIpAddress Strings like 199.212.0.6, 2001:500:13::6, or 10.0.0.0/8 (10/8 won't work)
     * @return CIDR object, or null if it fails to generate a CIDR
     */
    public static CIDR getCIDR( String rawIpAddress )
    {
        CIDR cidr = null;

        String cidrString = "";
        int index = rawIpAddress.indexOf( "/" );
        if ( index < 0 )
        {
            if ( IPAddr.isIPv4Address( rawIpAddress ) )
            {
                cidrString = rawIpAddress + "/32";
            }
            if ( IPAddr.isIPv6Address( rawIpAddress ) )
            {
                cidrString = rawIpAddress + "/128";
            }
        }
        else
        {
            cidrString = rawIpAddress;
        }

        try
        {
            if ( StringUtils.isNotEmpty( cidrString ) )
            {
                cidr = CIDR.generateCIDR( cidrString );
            }
        }
        catch ( UnknownHostException e )
        {
            // Ignore.
        }

        return cidr;
    }
}
