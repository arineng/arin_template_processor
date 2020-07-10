package net.arin.tp.processor.transform;

import net.arin.tp.ipaddr.CIDR;
import net.arin.tp.ipaddr.IPAddr;
import net.arin.tp.ipaddr.IPRange;
import net.arin.tp.ipaddr.IPVersion;
import net.arin.tp.processor.exception.TemplateException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NetTransformerTest
{
    @Test
    public void testConvertIPAddressFieldToIPRangeStartAndCidrPatterMatcherNoSpaces()
    {
        String netAddress = "192.168.000.000/24";

        IPRange ipRange = NetTransformer.convertIPAddressFieldToIPRange( netAddress, IPVersion.IPV4 );

        Assert.assertEquals( ipRange.getStartIPAddress().toFullNotation(), "192.168.000.000" );
        Assert.assertEquals( ipRange.getCidrs().get( 0 ).getPrefixLength(), 24 );

        netAddress = "7777:0000:1111:0000:0000:0000:0000:0000/64";

        ipRange = NetTransformer.convertIPAddressFieldToIPRange( netAddress, IPVersion.IPV6 );

        Assert.assertEquals( ipRange.getStartIPAddress().toFullNotation(), "7777:0000:1111:0000:0000:0000:0000:0000" );
        Assert.assertEquals( ipRange.getCidrs().get( 0 ).getPrefixLength(), 64 );

        netAddress = "260F:F000::/64";

        ipRange = NetTransformer.convertIPAddressFieldToIPRange( netAddress, IPVersion.IPV6 );

        Assert.assertEquals( ipRange.getStartIPAddress().toShortNotation(), "260F:F000::" );
        Assert.assertEquals( ipRange.getCidrs().get( 0 ).getPrefixLength(), 64 );
    }

    @Test
    public void testConvertIPAddressFieldToIPRangeStartAndCidrPatterMatcherSpaces()
    {
        String netAddress = "192.168.000.000 / 24";

        IPRange ipRange = NetTransformer.convertIPAddressFieldToIPRange( netAddress, IPVersion.IPV4 );

        Assert.assertEquals( ipRange.getStartIPAddress().toFullNotation(), "192.168.000.000" );
        Assert.assertEquals( ipRange.getCidrs().get( 0 ).getPrefixLength(), 24 );

        netAddress = "7777:0000:1111:0000:0000:0000:0000:0000 / 64";

        ipRange = NetTransformer.convertIPAddressFieldToIPRange( netAddress, IPVersion.IPV6 );

        Assert.assertEquals( ipRange.getStartIPAddress().toFullNotation(), "7777:0000:1111:0000:0000:0000:0000:0000" );
        Assert.assertEquals( ipRange.getCidrs().get( 0 ).getPrefixLength(), 64 );

        netAddress = "260F:F000:: / 64";

        ipRange = NetTransformer.convertIPAddressFieldToIPRange( netAddress, IPVersion.IPV6 );

        Assert.assertEquals( ipRange.getStartIPAddress().toShortNotation(), "260F:F000::" );
        Assert.assertEquals( ipRange.getCidrs().get( 0 ).getPrefixLength(), 64 );
    }

    @Test
    public void testConvertIPAddressFieldToIPRangeStartAndEndPatterMatcherNoSpaces()
    {
        String netAddress = "192.168.000.000-212.212.000.000";

        IPRange ipRange = NetTransformer.convertIPAddressFieldToIPRange( netAddress, IPVersion.IPV4 );

        Assert.assertEquals( ipRange.getStartIPAddress().toFullNotation(), "192.168.000.000" );
        Assert.assertEquals( ipRange.getEndIPAddress().toFullNotation(), "212.212.000.000" );

        netAddress = "7777:0000:1111:0000:0000:0000:0000:0000-7777:0000:1111:FFFF:FFFF:FFFF:FFFF:FFFF";

        ipRange = NetTransformer.convertIPAddressFieldToIPRange( netAddress, IPVersion.IPV6 );

        Assert.assertEquals( ipRange.getStartIPAddress().toFullNotation(), "7777:0000:1111:0000:0000:0000:0000:0000" );
        Assert.assertEquals( ipRange.getEndIPAddress().toFullNotation(), "7777:0000:1111:FFFF:FFFF:FFFF:FFFF:FFFF" );
    }

    @Test
    public void testConvertIPAddressFieldToIPRangeStartAndEndPatterMatcherSpaces()
    {
        String netAddress = "192.168.000.000 - 212.212.000.000";

        IPRange ipRange = NetTransformer.convertIPAddressFieldToIPRange( netAddress, IPVersion.IPV4 );

        Assert.assertEquals( ipRange.getStartIPAddress().toFullNotation(), "192.168.000.000" );
        Assert.assertEquals( ipRange.getEndIPAddress().toFullNotation(), "212.212.000.000" );

        netAddress = "7777:0000:1111:0000:0000:0000:0000:0000 - 7777:0000:1111:FFFF:FFFF:FFFF:FFFF:FFFF";

        ipRange = NetTransformer.convertIPAddressFieldToIPRange( netAddress, IPVersion.IPV6 );

        Assert.assertEquals( ipRange.getStartIPAddress().toFullNotation(), "7777:0000:1111:0000:0000:0000:0000:0000" );
        Assert.assertEquals( ipRange.getEndIPAddress().toFullNotation(), "7777:0000:1111:FFFF:FFFF:FFFF:FFFF:FFFF" );
    }

    @Test
    public void testConvertIPAddressFieldToIPRangeForValidIpv4() throws Exception
    {
        testConvertIPAddressFieldToIPRange( new IPAddr( "192.34.248.0" ), 21 );
    }

    @Test( expectedExceptions = TemplateException.class )
    public void testConvertIPAddressFieldToIPRangeForMisalignedIpv4Cidr() throws Exception
    {
        testConvertIPAddressFieldToIPRange( new IPAddr( "192.34.248.0" ), 19 );
    }

    @Test
    public void testConvertIPAddressFieldToIPRangeForValidIpv6() throws Exception
    {
        testConvertIPAddressFieldToIPRange( new IPAddr( "1234:abcd:e8f9::" ), 64 );
    }

    @Test( expectedExceptions = TemplateException.class )
    public void testConvertIPAddressFieldToIPRangeForInvalidIpv4()
    {
        NetTransformer.convertIPAddressFieldToIPRange( "1.21.gigawatts/55", IPVersion.IPV4 );
    }

    @Test( expectedExceptions = TemplateException.class )
    public void testConvertIPAddressFieldToIPRangeForInvalidIpv6()
    {
        NetTransformer.convertIPAddressFieldToIPRange( "feed:beef:moo::/00", IPVersion.IPV6 );
    }

    @Test
    public void testConvertIPAddressFieldToIPRangeInvalidMessage()
    {
        String netAddress = "INVALID";

        try
        {
            NetTransformer.convertIPAddressFieldToIPRange( netAddress, IPVersion.IPV4 );
        }
        catch ( TemplateException e )
        {
            Assert.assertEquals( e.getMessage(), "The format for IP Address is: 10.0.0.0/24 or 10.0.0.0 - 10.0.0.255" );
        }
    }

    private void testConvertIPAddressFieldToIPRange( IPAddr startIp, int cidrLength ) throws Exception
    {
        CIDR cidr = CIDR.findContainingCIDR( startIp, cidrLength );
        IPRange ipRange = NetTransformer.convertIPAddressFieldToIPRange( startIp.toShortNotation() + "/" + cidrLength,
                startIp.getVersion() );

        Assert.assertEquals( ipRange.getStartIPAddress(), startIp );
        Assert.assertEquals( ipRange.getCidrs().get( 0 ).getPrefixLength(), cidrLength );
        Assert.assertEquals( ipRange.getEndIPAddress(), cidr.getEndAddress() );
    }
}
