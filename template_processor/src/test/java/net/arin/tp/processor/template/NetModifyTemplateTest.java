package net.arin.tp.processor.template;

import net.arin.tp.ipaddr.IPVersion;
import net.arin.tp.processor.BaseTest;
import net.arin.tp.processor.util.MockTemplates;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class NetModifyTemplateTest extends BaseTest
{
    @Test
    public void testCreateV4TemplateIPV4Modify()
    {
        TemplateImpl template = TemplateImpl.createTemplate( MockTemplates.getNetModTemplate( "4.1", IPVersion.IPV4, TemplateImpl.Action.MODIFY ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV4NetModifyTemplateV4Impl );
        assertValuesMatch( ( IPV4NetModifyTemplateV4Impl ) template, "4.1", IPVersion.IPV4, TemplateImpl.Action.MODIFY );
    }

    @Test
    public void testCreateV5TemplateIPV4Modify()
    {
        TemplateImpl template = TemplateImpl.createTemplate( MockTemplates.getNetModTemplate( "5.0", IPVersion.IPV4, TemplateImpl.Action.MODIFY ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV4NetModifyTemplateV5Impl );
        assertValuesMatch( ( IPV4NetModifyTemplateV5Impl ) template, "5.0", IPVersion.IPV4, TemplateImpl.Action.MODIFY );
    }

    @Test
    public void testParseV4TemplateIPV4Modify()
    {
        IPV4NetModifyTemplateV4Impl template = new IPV4NetModifyTemplateV4Impl( MockTemplates.getNetModTemplate( "4.1", IPVersion.IPV4, TemplateImpl.Action.MODIFY ) );
        assertValuesMatch( template, "4.0", IPVersion.IPV4, TemplateImpl.Action.MODIFY );
    }

    @Test
    public void testParseV5TemplateIPV4Modify()
    {
        IPV4NetModifyTemplateV5Impl template = new IPV4NetModifyTemplateV5Impl( MockTemplates.getNetModTemplate( "5.0", IPVersion.IPV4, TemplateImpl.Action.MODIFY ) );
        assertValuesMatch( template, "5.0", IPVersion.IPV4, TemplateImpl.Action.MODIFY );
    }

    @Test
    public void testCreateV4TemplateIPV6Modify()
    {
        TemplateImpl template = TemplateImpl.createTemplate( MockTemplates.getNetModTemplate( "4.1", IPVersion.IPV6, TemplateImpl.Action.MODIFY ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV6NetModifyTemplateV4Impl );
        assertValuesMatch( ( IPV6NetModifyTemplateV4Impl ) template, "4.1", IPVersion.IPV6, TemplateImpl.Action.MODIFY );
    }

    @Test
    public void testCreateV5TemplateIPV6Modify()
    {
        TemplateImpl template = TemplateImpl.createTemplate( MockTemplates.getNetModTemplate( "5.0", IPVersion.IPV6, TemplateImpl.Action.MODIFY ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV6NetModifyTemplateV5Impl );
        assertValuesMatch( ( IPV6NetModifyTemplateV5Impl ) template, "5.0", IPVersion.IPV6, TemplateImpl.Action.MODIFY );
    }

    @Test
    public void testParseV4TemplateIPV6Modify()
    {
        IPV6NetModifyTemplateV4Impl template = new IPV6NetModifyTemplateV4Impl( MockTemplates.getNetModTemplate( "4.1", IPVersion.IPV6, TemplateImpl.Action.MODIFY ) );
        assertValuesMatch( template, "4.0", IPVersion.IPV6, TemplateImpl.Action.MODIFY );
    }

    @Test
    public void testParseV5TemplateIPV6Modify()
    {
        IPV6NetModifyTemplateV5Impl template = new IPV6NetModifyTemplateV5Impl( MockTemplates.getNetModTemplate( "5.0", IPVersion.IPV6, TemplateImpl.Action.MODIFY ) );
        assertValuesMatch( template, "5.0", IPVersion.IPV6, TemplateImpl.Action.MODIFY );
    }

    @Test
    public void testCreateV4TemplateIPV4Remove()
    {
        TemplateImpl template = TemplateImpl.createTemplate( MockTemplates.getNetModTemplate( "4.1", IPVersion.IPV4, TemplateImpl.Action.REMOVE ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV4NetModifyTemplateV4Impl );
        assertValuesMatch( ( IPV4NetModifyTemplateV4Impl ) template, "4.1", IPVersion.IPV4, TemplateImpl.Action.REMOVE );
    }

    @Test
    public void testCreateV5TemplateIPV4Remove()
    {
        TemplateImpl template = TemplateImpl.createTemplate( MockTemplates.getNetModTemplate( "5.0", IPVersion.IPV4, TemplateImpl.Action.REMOVE ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV4NetModifyTemplateV5Impl );
        assertValuesMatch( ( IPV4NetModifyTemplateV5Impl ) template, "5.0", IPVersion.IPV4, TemplateImpl.Action.REMOVE );
    }

    @Test
    public void testParseV4TemplateIPV4Remove()
    {
        IPV4NetModifyTemplateV4Impl template = new IPV4NetModifyTemplateV4Impl( MockTemplates.getNetModTemplate( "4.1", IPVersion.IPV4, TemplateImpl.Action.REMOVE ) );
        assertValuesMatch( template, "4.0", IPVersion.IPV4, TemplateImpl.Action.REMOVE );
    }

    @Test
    public void testParseV5TemplateIPV4Remove()
    {
        IPV4NetModifyTemplateV5Impl template = new IPV4NetModifyTemplateV5Impl( MockTemplates.getNetModTemplate( "5.0", IPVersion.IPV4, TemplateImpl.Action.REMOVE ) );
        assertValuesMatch( template, "5.0", IPVersion.IPV4, TemplateImpl.Action.REMOVE );
    }

    @Test
    public void testCreateV4TemplateIPV6Remove()
    {
        TemplateImpl template = TemplateImpl.createTemplate( MockTemplates.getNetModTemplate( "4.1", IPVersion.IPV6, TemplateImpl.Action.REMOVE ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV6NetModifyTemplateV4Impl );
        assertValuesMatch( ( IPV6NetModifyTemplateV4Impl ) template, "4.1", IPVersion.IPV6, TemplateImpl.Action.REMOVE );
    }

    @Test
    public void testCreateV5TemplateIPV6Remove()
    {
        TemplateImpl template = TemplateImpl.createTemplate( MockTemplates.getNetModTemplate( "5.0", IPVersion.IPV6, TemplateImpl.Action.REMOVE ) );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof IPV6NetModifyTemplateV5Impl );
        assertValuesMatch( ( IPV6NetModifyTemplateV5Impl ) template, "5.0", IPVersion.IPV6, TemplateImpl.Action.REMOVE );
    }

    @Test
    public void testParseV4TemplateIPV6Remove()
    {
        IPV6NetModifyTemplateV4Impl template = new IPV6NetModifyTemplateV4Impl( MockTemplates.getNetModTemplate( "4.1", IPVersion.IPV6, TemplateImpl.Action.REMOVE ) );
        assertValuesMatch( template, "4.0", IPVersion.IPV6, TemplateImpl.Action.REMOVE );
    }

    @Test
    public void testParseV5TemplateIPV6Remove()
    {
        IPV6NetModifyTemplateV5Impl template = new IPV6NetModifyTemplateV5Impl( MockTemplates.getNetModTemplate( "5.0", IPVersion.IPV6, TemplateImpl.Action.REMOVE ) );
        assertValuesMatch( template, "5.0", IPVersion.IPV6, TemplateImpl.Action.REMOVE );
    }

    private void assertValuesMatch( NetModifyTemplateImpl template, String version, IPVersion ipVersion, TemplateImpl.Action action )
    {
        if ( ipVersion.equals( IPVersion.IPV4 ) )
        {
            Assert.assertEquals( template.getTemplateName(), "ARIN-NET-MOD" );
        }
        else if ( ipVersion.equals( IPVersion.IPV6 ) )
        {
            Assert.assertEquals( template.getTemplateName(), "ARIN-IPv6-NET-MOD" );
        }

        if ( "5.0".equals( version ) )
        {
            Assert.assertEquals( template.getApiKey(), "API-0000-0000-0000-0000" );

            Assert.assertEquals( template.getTemplateVersion(), "5.0" );
        }
        else
        {
            Assert.assertEquals( template.getTemplateVersion(), "4.1" );
            Assert.assertEquals( template.getAdditionalInfo(), Arrays.asList( "This is some additional information." ) );
        }

        Assert.assertNotNull( template );
        Assert.assertEquals( template.getAction(), action );
        if ( ipVersion.equals( IPVersion.IPV4 ) )
        {
            Assert.assertEquals( template.getIpAddress(), "10.0.0.0 - 10.0.0.255" );
        }
        else if ( ipVersion.equals( IPVersion.IPV6 ) )
        {
            Assert.assertEquals( template.getIpAddress(), "2001:0100:0100::/48" );
        }
        Assert.assertEquals( template.getNetName(), "Net Name" );
        Assert.assertEquals( template.getOriginAses(), Arrays.asList( "AS1" ) );
        Assert.assertEquals( template.getTechPocHandles(), Arrays.asList( "TECHPOC" ) );
        Assert.assertEquals( template.getAbusePocHandles(), Arrays.asList( "ABUSEPOC" ) );
        Assert.assertEquals( template.getNocPocHandles(), Arrays.asList( "NOCPOC" ) );
        Assert.assertEquals( template.getPublicComments(), Arrays.asList( "These are comments for the public!" ) );
    }
}
