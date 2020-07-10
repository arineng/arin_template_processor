// Id: CIDR.java,v 1.2 2004/04/16 17:04:03 davidb Exp
//
// Copyright (C) 2002, 2004 VeriSign, Inc.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation; either version 2.1 of
// the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
// USA
package net.arin.tp.ipaddr;

import net.arin.tp.utils.UnknownHostRuntimeException;

import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is a base class for representing a CIDR notation networks.
 * <p>
 * Don't use this class to look for ending IP addresses of NETs! See the JavaDoc on the constructor for the reason why.
 *
 * @author David Blacka (original)
 */
public class CIDR implements Comparable<CIDR>
{
    private IPAddr prefix;
    private int prefixLength;

    /**
     * This essentially should never be used.
     */
    protected CIDR()
    {
    }

    /**
     * Construct the CIDR of size prefixLength that contains anIpAddress. This CIDR is NOT guaranteed to have a start
     * address that coincides with anIpAddress!
     * <p>
     * The most important thing to remember is that if you are looking for the end address of a NET that starts with
     * anIpAddress and is of size prefixLength, this is NOT the correct method to use; precisely because the CIDR
     * returned is not guaranteed to start with the IP address you send in. Specifically, the NET you are looking for
     * may actually consist of multiple CIDR blocks (off-bit). Instead, try:
     * {@link net.arin.tp.ipaddr.IPAddr#getOtherSideOfBlock}.
     */
    protected CIDR( IPAddr anIpAddress, int prefixLength )
    {
        this.prefixLength = prefixLength;

        // Then we mask and store the (possibly) corrected form of the anIpAddress.
        BigInteger mask = prefixLengthToMask( prefixLength, anIpAddress.getVersion() );

        // Correct the anIpAddress if it is off-bit.
        this.prefix = new IPAddr( anIpAddress.toBigInteger().and( mask ), anIpAddress.getVersion() );
    }

    protected CIDR( BigInteger cheese, int prefixLength, IPVersion version )
    {
        this.prefixLength = prefixLength;

        BigInteger mask = prefixLengthToMask( prefixLength, version );

        this.prefix = new IPAddr( cheese.and( mask ), version );
    }

    public IPRange toRange()
    {
        return new IPRange( prefix, getEndAddress() );
    }

    public static BigInteger ipPrefixLengthToLength( CIDR cidr )
    {
        return prefixLengthToLength( cidr.getPrefixLength(), cidr.getPrefix().getVersion() );
    }

    public static boolean isCIDR( String cidr )
    {
        try
        {
            CIDR tmp = generateCIDR( cidr );

            return true;
        }
        catch ( Exception ex )
        {
            return false;
        }
    }

    /**
     * Construct the CIDR of the given prefix length that contains the given ip address. The CIDR returned is NOT
     * guaranteed to start with the given ip address! See doc for: {@link net.arin.tp.ipaddr.CIDR#CIDR(IPAddr, int)}.
     */
    public static CIDR findContainingCIDR( IPAddr ip, int prefixLength ) throws UnknownHostException
    {
        if ( prefixLength < 0 )
        {
            throw new UnknownHostException( "Invalid prefix length used: " + prefixLength );
        }

        if ( prefixLength > ip.getVersion().getNumberOfBits() )
        {
            throw new UnknownHostException( "Invalid prefix length used: " + prefixLength );
        }

        return new CIDR( ip, prefixLength );
    }

    /**
     * Construct the CIDR specified by the given String in CIDR notation. The CIDR returned is NOT guaranteed to start
     * with the ip address specified in the CIDR notation input! See doc for:
     * {@link net.arin.tp.ipaddr.CIDR#CIDR(IPAddr, int)}.
     *
     * @param cidr String specifying CIDR block, eg: "10.10.0.0/16"
     * @return CIDR object
     * @throws UnknownHostException
     */
    public static CIDR generateCIDR( String cidr ) throws UnknownHostException
    {
        int p = cidr.indexOf( "/" );
        if ( p < 0 )
        {
            throw new UnknownHostException( "Invalid CIDR notation used: " + cidr );
        }

        String addr = cidr.substring( 0, p );
        String prefixLenStr = cidr.substring( p + 1 );

        IPAddr prefix;
        try
        {
            prefix = new IPAddr( addr );
        }
        catch ( UnknownHostRuntimeException e )
        {
            throw new UnknownHostException( "Invalid prefix: " + addr );
        }
        int prefixLength = parseInt( prefixLenStr, -1 );

        if ( prefixLength < 0 )
        {
            throw new UnknownHostException( "Invalid prefix length used: " + prefixLenStr );
        }

        return findContainingCIDR( prefix, prefixLength );
    }

    public static List<CIDR> generateCIDRs( IPAddr startAddress, IPAddr endAddress )
    {
        if ( !startAddress.getClass().equals( endAddress.getClass() ) )
        {
            throw new IllegalArgumentException(
                    "startAddress and endAddress must both be the same kind of address (IPv4 vs. IPv6)." );
        }

        return calcCIDRs( startAddress, endAddress );
    }

    /**
     * @param intstr a string containing an integer
     * @param def    the default if the string does not contain a valid integer
     * @return the int
     */
    static int parseInt( String intstr, int def )
    {
        Integer res;

        if ( intstr == null )
        {
            return def;
        }

        try
        {
            res = Integer.decode( intstr );
        }
        catch ( Exception e )
        {
            res = new Integer( def );
        }

        return res.intValue();
    }

    public IPAddr getPrefix()
    {
        return prefix;
    }

    public int getPrefixLength()
    {
        return prefixLength;
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final CIDR other = ( CIDR ) obj;
        if ( !Objects.equals( this.prefix, other.prefix ) )
        {
            return false;
        }
        return this.prefixLength == other.prefixLength;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 89 * hash + ( this.prefix != null ? this.prefix.hashCode() : 0 );
        hash = 89 * hash + this.prefixLength;
        return hash;
    }

    /**
     * @return the cidr representation as a String in the form of "prefix/length"
     */
    public String toCidrString()
    {
        return prefix + "/" + prefixLength;
    }

    /**
     * @return the textual CIDR notation
     */
    @Override
    public String toString()
    {
        return toCidrString();
    }

    /**
     * @return the end address of this block
     */
    public IPAddr getEndAddress()
    {
        return new IPAddr( getEndAddressBigInt(), prefix.getVersion() );
    }

    protected BigInteger getEndAddressBigInt()
    {
        return prefix.toBigInteger().add( prefixLengthToLength( getPrefixLength(), prefix.getVersion() ) ).subtract( BigInteger.ONE );
    }


    public static List<CIDR> calcCIDRs( IPAddr startAddr, IPAddr endAddr )
    {
        IPVersion ipVersion = startAddr.getVersion();
        if ( endAddr.getVersion() != ipVersion )
        {
            throw new IllegalArgumentException( "IP versions of start and end of CIDR block do not match." );
        }

        if ( startAddr.compareTo( endAddr ) > 0 )
        {
            throw new IllegalArgumentException( "The start IP address must not be greater than the end address." );
        }

        // Get the starting and ending addresses as BigIntegers.
        BigInteger start = startAddr.toBigInteger();
        BigInteger end = endAddr.toBigInteger();

        // Calculate our block length.
        BigInteger blockLength = end.subtract( start ).add( BigInteger.ONE );

        // And our largest prefix length that will fit.
        int prefixLength = largestPrefix( blockLength, ipVersion );

        List<CIDR> res = new ArrayList<>();

        // While blockLength > 0.
        while ( blockLength.compareTo( BigInteger.ZERO ) > 0 )
        {
            BigInteger mask = prefixLengthToMask( prefixLength, ipVersion );

            // Check to see if the current prefix length is valid.
            if ( start.and( mask ).compareTo( start ) != 0 )
            {
                // If not, shrink the CIDR network length (i.e., lengthen the prefix).
                prefixLength++;
                continue;
            }

            // Otherwise, we have found a valid CIDR block, so add it to the result set.
            CIDR cidr = new CIDR( start, prefixLength, ipVersion );
            res.add( cidr );

            // And setup for the next round.
            BigInteger currentLength = prefixLengthToLength( prefixLength, ipVersion );
            start = start.add( currentLength );
            blockLength = blockLength.subtract( currentLength );
            prefixLength = largestPrefix( blockLength, ipVersion );
        }

        return res;
    }

    /**
     * Given a prefix length, return the block length. I.e., a prefix length of 96 for IPv6 will return 2**32.
     */
    public static BigInteger prefixLengthToLength( int prefixLength, IPVersion ipVersion )
    {
        return BigInteger.ONE.shiftLeft( ipVersion.getNumberOfBits() - prefixLength );
    }

    protected static BigInteger prefixLengthToMask( int prefixLength, IPVersion ipVersion )
    {
        return BigInteger.ONE.shiftLeft( ipVersion.getNumberOfBits() - prefixLength ).subtract( BigInteger.ONE ).not();
    }

    public static int largestPrefix( BigInteger length, IPVersion ipVersion )
    {
        return ipVersion.getNumberOfBits() - length.bitLength() + 1;
    }

    public int compareTo( CIDR o )
    {
        int r = getPrefix().compareTo( o.getPrefix() );
        if ( r != 0 )
        {
            return r;
        }
        return getEndAddress().compareTo( o.getEndAddress() );
    }
}
