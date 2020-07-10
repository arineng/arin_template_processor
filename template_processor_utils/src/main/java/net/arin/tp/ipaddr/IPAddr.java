package net.arin.tp.ipaddr;

import net.arin.tp.utils.UnknownHostRuntimeException;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddr implements Comparable<IPAddr>, IsIPVersionAware
{
    private static final BigInteger TWO = BigInteger.valueOf( 2 );

    public static final BigInteger hostIdMask = new BigInteger( "0000000000000000FFFFFFFFFFFFFFFF", 16 );
    public static final BigInteger validStart = BigInteger.ZERO;
    public static final BigInteger validEnd = hostIdMask;

    private final InetAddress asInetAddress;
    private final BigInteger asBigInteger;
    private volatile String asShortString;
    private volatile String asFullString;
    private volatile String asNonPaddedString;

    public static final String IPV4_REGEX = "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}";
    public static final String IPV4_REGEX_ANCHORED = "\\A" + IPV4_REGEX + "\\z";
    public static final String IPV6_HEX4DECCOMPRESSED_REGEX = "\\A((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}:)*)(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z";
    public static final String IPV6_6HEX4DEC_REGEX = "\\A((?:[0-9A-Fa-f]{1,4}:){6,6})(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z";
    public static final String IPV6_HEXCOMPRESSED_REGEX = "((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)";
    public static final String IPV6_HEXCOMPRESSED_REGEX_ANCHORED = "\\A" + IPV6_HEXCOMPRESSED_REGEX + "\\z";
    public static final String IPV6_REGEX = "(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}";
    public static final String IPV6_REGEX_ANCHORED = "\\A" + IPV6_REGEX + "\\z";
    public static final Pattern IPV4_REGEX_P = Pattern.compile( IPV4_REGEX_ANCHORED );
    public static final Pattern IPV6_HEX4DECCOMPRESSED_REGEX_P = Pattern.compile( IPV6_HEX4DECCOMPRESSED_REGEX );
    public static final Pattern IPV6_6HEX4DEC_REGEX_P = Pattern.compile( IPV6_6HEX4DEC_REGEX );
    public static final Pattern IPV6_HEXCOMPRESSED_REGEX_P = Pattern.compile( IPV6_HEXCOMPRESSED_REGEX_ANCHORED );
    public static final Pattern IPV6_REGEX_P = Pattern.compile( IPV6_REGEX_ANCHORED );

    private static final Pattern IPV6_ZERO_SEQ_P = Pattern.compile( "(^|:)0(:0)+($|:)" );

    public IPAddr( InetAddress asInetAddress )
    {
        if ( asInetAddress == null )
        {
            throw new NullPointerException( "IPAddress: cannot accept a null InetAddress." );
        }

        this.asInetAddress = asInetAddress;
        this.asBigInteger = new BigInteger( 1, asInetAddress.getAddress() );
    }

    public IPAddr( String asString )
    {
        this( toInetAddress( asString ) );
    }

    public IPAddr( BigInteger asBigInteger, IPVersion ipVersion )
    {
        this( toInetAddress( asBigInteger, ipVersion ) );
    }

    @Override
    public boolean equals( Object ipAddress )
    {
        return asInetAddress.equals( ( ( IPAddr ) ipAddress ).asInetAddress );
    }

    @Override
    public int hashCode()
    {
        return asInetAddress.hashCode();
    }

    /**
     * Compares this IPAddr to the argument IPAddr.
     *
     * @param other the other IPAddr to compare to
     * @return an int indicating whether or not this IPAddr was smaller, equal to, or larger than the argument IPAddr
     */
    public int compareTo( IPAddr other )
    {
        if ( this.getVersion() != other.getVersion() )
        {
            throw new RuntimeException( "can't compare v4 and v6 addresses (" + this.toShortNotation() + " and " + other.toShortNotation() + ")" );
        }

        // Don't need to ignore case here since the full notation that is stored when this object is created is
        // uppercased and ignore case is slow.
        return this.asBigInteger.compareTo( other.asBigInteger );
    }

    public BigInteger toBigInteger()
    {
        return asBigInteger;
    }

    /**
     * Returns a fully-padded String representation of this IPAddr like:
     * 008.024.000.255
     * ABCD:00F0:0000:0000:0000:0000:0000:0000
     */
    public String toFullNotation()
    {
        if ( asFullString == null )
        {
            asFullString = ipAddressToFullNotation( asInetAddress );
        }
        return asFullString;
    }

    /**
     * Returns a "short" String representation of this IPAddr like:
     * 8.24.0.255
     * ABCD:F0::
     * <p/>
     * For v4, this is the same as the non-padded notation. For v6, it truncates the trailing 16-bit groups that consist
     * of zeros.
     */
    public String toShortNotation()
    {
        if ( asShortString == null )
        {
            asShortString = ipAddressToShortNotation( asInetAddress );
        }
        return asShortString;
    }

    /**
     * Returns the short notation version of this IPAddr.
     */
    public String toString()
    {
        return toShortNotation();
    }

    public IPVersion getVersion()
    {
        if ( asInetAddress instanceof Inet4Address )
        {
            return IPVersion.IPV4;
        }
        else if ( asInetAddress instanceof Inet6Address )
        {
            return IPVersion.IPV6;
        }

        throw new IllegalArgumentException( "IPAddress: unknown IP version." );
    }

    public boolean isV4()
    {
        return getVersion() == IPVersion.IPV4;
    }

    public boolean isV6()
    {
        return getVersion() == IPVersion.IPV6;
    }

    /**
     * Build a new IPAddr by adding numberOfAddresses to this one. The original IPAddr is left unchanged.
     */
    public IPAddr add( BigInteger numberOfAddresses )
    {
        return new IPAddr( this.toBigInteger().add( numberOfAddresses ), this.getVersion() );
    }

    public IPAddr getOtherSideOfBlock( int blockSize )
    {
        return this.add( CIDR.prefixLengthToLength( blockSize, this.getVersion() ).subtract( BigInteger.ONE ) );
    }

    /**
     * Uses a regular expression to determine if a string contains a valid IPv4 address. Use this method instead of
     * {@link java.net.InetAddress#getByName(String)} if you don't want to run the chance of invoking the naming service
     * for something that might not hold an IP address at all.
     *
     * @param addr a string containing an IPv4 dotted quad address
     * @return true if the string is a valid IPv4 address, otherwise false
     */
    public static boolean isIPv4Address( String addr )
    {
        return IPV4_REGEX_P.matcher( addr ).matches();
    }

    /**
     * Uses a series of regular expressions to determine if a string contains a valid IPv4 address. Use this method
     * instead of {@link java.net.InetAddress#getByName(String)} if you don't want to run the chance of invoking the
     * naming service for something that might not hold an IP address at all.
     *
     * @param addr a string containing an IPv6 address
     * @return true if the string is a valid IPv6 address, otherwise false
     */
    public static boolean isIPv6Address( String addr )
    {
        boolean retval = false;
        if ( IPV6_6HEX4DEC_REGEX_P.matcher( addr ).matches() )
        {
            retval = true;
        }
        else if ( IPV6_HEX4DECCOMPRESSED_REGEX_P.matcher( addr ).matches() )
        {
            retval = true;
        }
        else if ( IPV6_HEXCOMPRESSED_REGEX_P.matcher( addr ).matches() )
        {
            retval = true;
        }
        else if ( IPV6_REGEX_P.matcher( addr ).matches() )
        {
            retval = true;
        }
        return retval;
    }

    public static IPAddr ipAddrIfValid( String ip )
    {
        try
        {
            return new IPAddr( ip );
        }
        catch ( Exception e )
        {
            return null;
        }
    }

    /**
     * Convert a big integer into an IPv6 address.
     *
     * @param addr    the address as a big integer
     * @param version the ip version
     * @return the inet address object
     * @throws UnknownHostException if the big integer is too large, and thus an invalid IPv6 address
     */
    private static InetAddress bigIntToIPAddress( BigInteger addr, IPVersion version ) throws UnknownHostException
    {
        int numberOfBytes = version.getNumberOfBits() / 8; // The definition of a byte hasn't changed; still 8 bits.
        byte[] a = new byte[numberOfBytes];
        byte[] b = addr.toByteArray();

        if ( b.length > numberOfBytes && !( b.length == ( numberOfBytes + 1 ) && b[0] == 0 ) )
        {
            throw new UnknownHostException( "invalid IP address (too big)" );
        }

        if ( b.length == numberOfBytes )
        {
            return IpAddrUtils.getByAddress( b );
        }

        // Handle the case where the IPv6 address starts with "FF".
        if ( b.length == ( numberOfBytes + 1 ) )
        {
            System.arraycopy( b, 1, a, 0, numberOfBytes );
        }
        else
        {
            // Copy the address into a 16 byte array, zero-filled.
            int p = numberOfBytes - b.length;
            for ( int i = 0; i < b.length; i++ )
            {
                a[p + i] = b[i];
            }
        }
        return IpAddrUtils.getByAddress( a );
    }

    /**
     * Returns the non-padded notation of a IPv4 or IPv6 address.
     * <p/>
     * IPv4 address "010.001.000.200" would be "10.1.0.200"
     * IPv6 address "1001:0400:0000:00FF::" would be "1001:400:0:FF::"
     */
    private static String ipAddressToNonPaddedNotation( InetAddress inetAddress )
    {
        byte[] address = inetAddress.getAddress();

        if ( inetAddress instanceof Inet4Address )
        {
            return String.format( "%d.%d.%d.%d",
                    address[0] & 0xFF, address[1] & 0xFF, address[2] & 0xFF, address[3] & 0xFF );
        }
        else if ( inetAddress instanceof Inet6Address )
        {
            return String.format( "%X:%X:%X:%X:%X:%X:%X:%X",
                    bytePairToInt( address[0], address[1] ), bytePairToInt( address[2], address[3] ),
                    bytePairToInt( address[4], address[5] ), bytePairToInt( address[6], address[7] ),
                    bytePairToInt( address[8], address[9] ), bytePairToInt( address[10], address[11] ),
                    bytePairToInt( address[12], address[13] ), bytePairToInt( address[14], address[15] ) );
        }
        else
        {
            // This should never ever happen.
            throw new IllegalArgumentException( "'" + inetAddress.getClass().getName() + "' is not a supported InetAddress type" );
        }
    }

    /**
     * Returns the full notation for an IP address. For IPv4 addresses, it is the quad decimal format with period
     * separators, with each decimal number zero padded to three spaces (e.g. '010.000.000.255'). For IPv6 addresses,
     * it is the octal hexadecimal format with colon separators, with each hexadecimal number zero padded to 4 spaces
     * (e.g. '2001:0468:0FFF:FFFF:FFFF:FFFF:FFFF:FFFF').
     *
     * @param inetAddress the IP address to convert
     * @return the fully notated IP address
     */
    private static String ipAddressToFullNotation( InetAddress inetAddress )
    {
        byte[] address = inetAddress.getAddress();

        if ( inetAddress instanceof Inet4Address )
        {
            return String.format( "%03d.%03d.%03d.%03d",
                    address[0] & 0xFF, address[1] & 0xFF, address[2] & 0xFF, address[3] & 0xFF );
        }
        else if ( inetAddress instanceof Inet6Address )
        {
            return String.format( "%04X:%04X:%04X:%04X:%04X:%04X:%04X:%04X",
                    bytePairToInt( address[0], address[1] ), bytePairToInt( address[2], address[3] ),
                    bytePairToInt( address[4], address[5] ), bytePairToInt( address[6], address[7] ),
                    bytePairToInt( address[8], address[9] ), bytePairToInt( address[10], address[11] ),
                    bytePairToInt( address[12], address[13] ), bytePairToInt( address[14], address[15] ) );
        }
        else
        {
            // This should never ever happen.
            throw new IllegalArgumentException( "'" + inetAddress.getClass().getName() + "' is not a supported InetAddress type" );
        }
    }

    private static int bytePairToInt( byte b1, byte b2 )
    {
        return ( ( b1 & 0xFF ) << 8 ) | ( b2 & 0xFF );
    }

    /**
     * Returns the short notation for an IP address.
     *
     * @param inetAddress an {@link java.net.Inet4Address} or {@link java.net.Inet6Address} instance
     * @return the short notation for ipAddress
     */
    private static String ipAddressToShortNotation( InetAddress inetAddress )
    {
        String regularNotation = ipAddressToNonPaddedNotation( inetAddress );

        if ( inetAddress instanceof Inet4Address )
        {
            return regularNotation;
        }
        else if ( inetAddress instanceof Inet6Address )
        {
            int maxZeroSeqStart = -1;
            int maxZeroSeqEnd = -1;
            Matcher m = IPV6_ZERO_SEQ_P.matcher( regularNotation );
            while ( m.find() )
            {
                if ( ( m.end() - m.start() ) / 2 > ( maxZeroSeqEnd - maxZeroSeqStart ) / 2 )
                {
                    maxZeroSeqStart = m.start();
                    maxZeroSeqEnd = m.end();
                }
            }

            if ( maxZeroSeqStart < 0 )
            {
                return regularNotation;
            }
            else
            {
                return regularNotation.substring( 0, maxZeroSeqStart ) + "::" + regularNotation.substring( maxZeroSeqEnd );
            }
        }
        else
        {
            // This should never ever happen.
            throw new IllegalArgumentException( "'" + inetAddress.getClass().getName() + "' is not a supported InetAddress type" );
        }
    }

    private static InetAddress toInetAddress( BigInteger asBigInteger, final IPVersion ipVersion )
    {
        if ( asBigInteger.compareTo( TWO.pow( ipVersion.getNumberOfBits() ) ) >= 0 )
        {
            throw new IllegalArgumentException( String.format( "IPAddress: [%s] is too big.", asBigInteger ) );
        }

        if ( asBigInteger.compareTo( BigInteger.ZERO ) < 0 )
        {
            throw new IllegalArgumentException( String.format( "IPAddress: [%s] is too small.", asBigInteger ) );
        }

        try
        {
            return bigIntToIPAddress( asBigInteger, ipVersion );
        }
        catch ( UnknownHostException e )
        {
            throw new UnknownHostRuntimeException( "Unable to convert BigInteger to ip address [" + asBigInteger + "] of version [" + ipVersion + "]", e );
        }
    }

    private static InetAddress toInetAddress( String asString )
    {
        try
        {
            if ( isIPv4Address( asString ) )
            {
                return IpAddrUtils.getByAddress( IpAddrUtils.textToNumericFormatV4( asString ) );
            }
            else if ( isIPv6Address( asString ) )
            {
                return IpAddrUtils.getByAddress( IpAddrUtils.textToNumericFormatV6( asString ) );
            }
        }
        catch ( UnknownHostException unknownHostException )
        {
            throw new UnknownHostRuntimeException( unknownHostException );
        }

        throw new UnknownHostRuntimeException( String.format( "[%s] is not a valid IP.", asString ) );
    }
}
