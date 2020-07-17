package net.arin.tp.api.payload;

import net.arin.tp.api.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class NetPayloadTest extends BaseTest
{
    @Test
    public void testEquality()
    {
        NetPayload np = getTestNetPayload();

        NetPayload np2 = getTestNetPayload();

        Assert.assertEquals( np, np2 );
        Assert.assertEquals( np2, np );

        // Case doesn't matter for these elements - and we'll add some white space that should be trimmed as well.
        np2.setNetHandle( np2.getNetHandle().toLowerCase() + "  " );
        np2.setParentNetHandle( " " + np2.getParentNetHandle().toLowerCase() + "  " );
        np2.setOrgHandle( np2.getOrgHandle().toLowerCase() + " " );
        np2.setNetName( np2.getNetName().toLowerCase() + "   " );
        Assert.assertEquals( np, np2 );

        // Origin ASes in different orders should still be equal.
        NetPayload diffOriginAsesByOrder = getTestNetPayload();
        diffOriginAsesByOrder.getOriginAses().clear();
        diffOriginAsesByOrder.getOriginAses().addAll( Arrays.asList( "456", "123" ) );
        Assert.assertEquals( np, diffOriginAsesByOrder );
        Assert.assertEquals( diffOriginAsesByOrder, np );

        // Origin ASes that have the "AS" prefix and those that don't should still be considered logically equal.
        NetPayload diffOriginAsesByASText = getTestNetPayload();
        diffOriginAsesByASText.getOriginAses().clear();
        diffOriginAsesByASText.getOriginAses().addAll( Arrays.asList( "AS123  ", "AS456" ) );  // intentional whitespace
        Assert.assertEquals( np, diffOriginAsesByASText );
        Assert.assertEquals( diffOriginAsesByASText, np );

        PocLinkPayload plp = new PocLinkPayload( "POC-HAND1", PocLinkPayload.Function.AD );
        PocLinkPayload plp2 = new PocLinkPayload( "  POC-HAND2  ", PocLinkPayload.Function.T ); // intentional whitespace
        PocLinkPayload plp3 = new PocLinkPayload( "POC-HAND3", PocLinkPayload.Function.T );

        // POC links represent the same POCs, but in a different order.
        NetPayload diffPocLinksByOrder = getTestNetPayload();
        diffPocLinksByOrder.getPocLinks().clear();
        diffPocLinksByOrder.getPocLinks().addAll( Arrays.asList( plp3, plp, plp2 ) );
        Assert.assertEquals( np, diffPocLinksByOrder );
        Assert.assertEquals( diffPocLinksByOrder, np );
    }

    @Test
    public void testInequality()
    {
        NetPayload standardNet = getTestNetPayload();

        NetPayload netHandleDiff = getTestNetPayload();
        netHandleDiff.setNetHandle( "WHATEVER" );
        Assert.assertFalse( standardNet.equals( netHandleDiff ) );
        Assert.assertFalse( netHandleDiff.equals( standardNet ) );

        NetPayload orgHandleDiff = getTestNetPayload();
        orgHandleDiff.setOrgHandle( "WHATEVER" );
        Assert.assertFalse( standardNet.equals( orgHandleDiff ) );
        Assert.assertFalse( orgHandleDiff.equals( standardNet ) );

        NetPayload parentNetHandleDiff = getTestNetPayload();
        parentNetHandleDiff.setParentNetHandle( "WHATEVER" );
        Assert.assertFalse( standardNet.equals( parentNetHandleDiff ) );
        Assert.assertFalse( parentNetHandleDiff.equals( standardNet ) );

        NetPayload netNameDiff = getTestNetPayload();
        netNameDiff.setNetName( "WHATEVER" );
        Assert.assertFalse( standardNet.equals( netNameDiff ) );
        Assert.assertFalse( netNameDiff.equals( standardNet ) );

        NetPayload versionDiff = getTestNetPayload();
        versionDiff.setVersion( 6 );
        Assert.assertFalse( standardNet.equals( versionDiff ) );
        Assert.assertFalse( versionDiff.equals( standardNet ) );

        NetPayload noNetBlock = getTestNetPayload();
        noNetBlock.getNetBlocks().clear();
        Assert.assertFalse( standardNet.equals( noNetBlock ) );
        Assert.assertFalse( noNetBlock.equals( standardNet ) );

        NetPayload diffNetBlock = getTestNetPayload();
        diffNetBlock.getNetBlocks().get( 0 ).setType( NetBlockPayload.NetType.DIRECT_ALLOCATION );
        Assert.assertFalse( standardNet.equals( diffNetBlock ) );
        Assert.assertFalse( diffNetBlock.equals( standardNet ) );

        NetPayload noOriginAses = getTestNetPayload();
        noOriginAses.getOriginAses().clear();
        Assert.assertFalse( standardNet.equals( noOriginAses ) );
        Assert.assertFalse( noOriginAses.equals( standardNet ) );

        NetPayload diffOriginAses = getTestNetPayload();
        diffOriginAses.getOriginAses().clear();
        diffOriginAses.getOriginAses().addAll( Arrays.asList( "555", "999" ) );
        Assert.assertFalse( standardNet.equals( diffOriginAses ) );
        Assert.assertFalse( diffOriginAses.equals( standardNet ) );

        NetPayload diffPubComment = getTestNetPayload();
        diffPubComment.setPublicComments( "something different" );
        Assert.assertFalse( standardNet.equals( diffPubComment ) );
        Assert.assertFalse( diffPubComment.equals( standardNet ) );
    }

    private NetPayload getTestNetPayload()
    {
        NetPayload np = new NetPayload();

        np.setNetHandle( "NET-HANDLE-123" );
        np.setOrgHandle( "JC-ORG" );
        np.setParentNetHandle( "NET-HANDLE-123-PAR" );
        np.setNetName( "This is a net name" );
        np.setVersion( 4 );

        NetBlockPayload nbp = new NetBlockPayload();
        nbp.setStartAddress( "255.0.0.0" );
        nbp.setEndAddress( "255.255.255.255" );
        nbp.setCidrLength( "8" );
        nbp.setType( NetBlockPayload.NetType.REASSIGNED );

        np.getNetBlocks().add( nbp );

        np.getOriginAses().addAll( Arrays.asList( "123", "456" ) );
        np.setPublicComments( "This is a public comment with just one line" );

        PocLinkPayload plp = new PocLinkPayload( "POC-HAND1", PocLinkPayload.Function.AD );
        PocLinkPayload plp2 = new PocLinkPayload( "POC-HAND2", PocLinkPayload.Function.T );
        PocLinkPayload plp3 = new PocLinkPayload( "POC-HAND3", PocLinkPayload.Function.T );
        np.getPocLinks().add( plp );
        np.getPocLinks().add( plp2 );
        np.getPocLinks().add( plp3 );

        return np;
    }
}
