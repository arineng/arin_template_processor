package net.arin.tp.api.payload;

import net.arin.tp.api.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PocLinkPayloadTest extends BaseTest
{
    @Test
    public void testEquality()
    {
        PocLinkPayload plp1 = new PocLinkPayload( "POC_HANDLE", PocLinkPayload.Function.AD );
        PocLinkPayload plp2 = new PocLinkPayload( "POC_HANDLE", PocLinkPayload.Function.AD );
        Assert.assertEquals( plp1, plp2 );

        // Handle case should not matter.
        plp2.setHandle( plp2.getHandle().toLowerCase() );
        Assert.assertEquals( plp1, plp2 );
    }

    @Test
    public void testInequality()
    {
        PocLinkPayload plp1 = new PocLinkPayload( "POC_HANDLE", PocLinkPayload.Function.AD );

        PocLinkPayload functionDiff = new PocLinkPayload( "POC_HANDLE", PocLinkPayload.Function.N );
        PocLinkPayload handleDiff = new PocLinkPayload( "POC_HANDLE_DIFF", PocLinkPayload.Function.AD );
        PocLinkPayload bothDiff = new PocLinkPayload( "POC_HANDLE_DIFF", PocLinkPayload.Function.N );

        Assert.assertFalse( plp1.equals( "String value not equal" ) );
        Assert.assertFalse( plp1.equals( functionDiff ) );
        Assert.assertFalse( plp1.equals( handleDiff ) );
        Assert.assertFalse( plp1.equals( bothDiff ) );

        Assert.assertFalse( functionDiff.equals( plp1 ) );
        Assert.assertFalse( handleDiff.equals( plp1 ) );
        Assert.assertFalse( bothDiff.equals( plp1 ) );
    }
}
