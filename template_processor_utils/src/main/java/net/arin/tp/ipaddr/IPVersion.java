package net.arin.tp.ipaddr;

public enum IPVersion
{
    IPV4( 4, 8, 32, 32 ),
    IPV6( 6, 4, 128, 64 );

    private int version;
    private int bitsInDelegation;
    private int maxBits;
    private int maxBitsForRouting;

    IPVersion( int version, int bitsInDelegation, int maxBits, int maxBitsForRouting )
    {
        this.version = version;
        this.bitsInDelegation = bitsInDelegation;
        this.maxBits = maxBits;
        this.maxBitsForRouting = maxBitsForRouting;
    }

    public String asString()
    {
        return "" + version;
    }

    public int getNumberOfBits()
    {
        return maxBits;
    }

    public int getNumberOfBitsInDelegation()
    {
        return bitsInDelegation;
    }

    public int getMaxBitsForRouting()
    {
        return maxBitsForRouting;
    }

    public int asInt()
    {
        return version;
    }

    public static IPVersion fromInt( int i )
    {
        for ( IPVersion ipVersion : IPVersion.values() )
        {
            if ( ipVersion.asInt() == i )
            {
                return ipVersion;
            }
        }
        throw new RuntimeException( "Invalid IP version of [" + i + "]" );
    }

    public static IPVersion fromIntString( String s )
    {
        return IPVersion.valueOf( "IPV" + s );
    }

    public static class UnsupportedVersionException extends RuntimeException
    {
        public UnsupportedVersionException( IPVersion ipVersion )
        {
            super( ipVersion.name() );
        }
    }
}
