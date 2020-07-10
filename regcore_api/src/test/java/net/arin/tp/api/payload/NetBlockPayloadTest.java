package net.arin.tp.api.payload;

import net.arin.tp.api.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NetBlockPayloadTest extends BaseTest
{
    @Test
    public void testEquality()
    {
        NetBlockPayload nbp1 = new NetBlockPayload();
        nbp1.setStartAddress( "255.0.0.0" );
        nbp1.setEndAddress( "255.255.255.255" );
        nbp1.setCidrLength( "8" );
        nbp1.setType( NetBlockPayload.NetType.REASSIGNED );

        NetBlockPayload nbp2 = new NetBlockPayload();
        nbp2.setStartAddress( "255.0.0.0" );
        nbp2.setEndAddress( "255.255.255.255" );
        nbp2.setCidrLength( "8" );
        nbp2.setType( NetBlockPayload.NetType.REASSIGNED );

        Assert.assertEquals( nbp1, nbp2 );
        Assert.assertEquals( nbp2, nbp1 );
    }

    @Test
    public void testInequality()
    {
        NetBlockPayload nbp1 = new NetBlockPayload();
        nbp1.setStartAddress( "255.0.0.0" );
        nbp1.setEndAddress( "255.255.255.255" );
        nbp1.setCidrLength( "8" );
        nbp1.setType( NetBlockPayload.NetType.REASSIGNED );

        NetBlockPayload startDiff = new NetBlockPayload();
        startDiff.setStartAddress( "255.0.0.1" );
        startDiff.setEndAddress( "255.255.255.255" );
        startDiff.setCidrLength( "8" );
        startDiff.setType( NetBlockPayload.NetType.REASSIGNED );

        NetBlockPayload startNull = new NetBlockPayload();
        startNull.setEndAddress( "255.255.255.255" );
        startNull.setCidrLength( "8" );
        startNull.setType( NetBlockPayload.NetType.REASSIGNED );

        NetBlockPayload endDiff = new NetBlockPayload();
        endDiff.setStartAddress( "255.0.0.0" );
        endDiff.setEndAddress( "254.255.255.255" );
        endDiff.setCidrLength( "8" );
        endDiff.setType( NetBlockPayload.NetType.REASSIGNED );

        NetBlockPayload endNull = new NetBlockPayload();
        endNull.setStartAddress( "255.0.0.0" );
        endNull.setCidrLength( "8" );
        endNull.setType( NetBlockPayload.NetType.REASSIGNED );

        NetBlockPayload cidrDiff = new NetBlockPayload();
        cidrDiff.setStartAddress( "255.0.0.0" );
        cidrDiff.setEndAddress( "255.255.255.255" );
        cidrDiff.setCidrLength( "24" );
        cidrDiff.setType( NetBlockPayload.NetType.REASSIGNED );

        NetBlockPayload cidrNull = new NetBlockPayload();
        cidrNull.setStartAddress( "255.0.0.0" );
        cidrNull.setEndAddress( "255.255.255.255" );
        cidrNull.setType( NetBlockPayload.NetType.REASSIGNED );

        NetBlockPayload typeDiff = new NetBlockPayload();
        typeDiff.setStartAddress( "255.0.0.0" );
        typeDiff.setEndAddress( "255.255.255.255" );
        typeDiff.setCidrLength( "8" );
        typeDiff.setType( NetBlockPayload.NetType.REALLOCATED );

        NetBlockPayload typeNull = new NetBlockPayload();
        typeNull.setStartAddress( "255.0.0.0" );
        typeNull.setEndAddress( "255.255.255.255" );
        typeNull.setCidrLength( "8" );

        Assert.assertFalse( nbp1.equals( "String value" ) );

        Assert.assertFalse( nbp1.equals( startDiff ) );
        Assert.assertFalse( nbp1.equals( startNull ) );
        Assert.assertFalse( nbp1.equals( endDiff ) );
        Assert.assertFalse( nbp1.equals( endNull ) );
        Assert.assertFalse( nbp1.equals( cidrDiff ) );
        Assert.assertFalse( nbp1.equals( cidrNull ) );
        Assert.assertFalse( nbp1.equals( typeDiff ) );
        Assert.assertFalse( nbp1.equals( typeNull ) );

        Assert.assertFalse( startDiff.equals( nbp1 ) );
        Assert.assertFalse( startNull.equals( nbp1 ) );
        Assert.assertFalse( endDiff.equals( nbp1 ) );
        Assert.assertFalse( endNull.equals( nbp1 ) );
        Assert.assertFalse( cidrDiff.equals( nbp1 ) );
        Assert.assertFalse( cidrNull.equals( nbp1 ) );
        Assert.assertFalse( typeDiff.equals( nbp1 ) );
        Assert.assertFalse( typeNull.equals( nbp1 ) );
    }
}
