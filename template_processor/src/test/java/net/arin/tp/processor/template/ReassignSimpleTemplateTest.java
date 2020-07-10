package net.arin.tp.processor.template;

import net.arin.tp.processor.BaseTest;
import net.arin.tp.processor.util.MockTemplates;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class ReassignSimpleTemplateTest extends BaseTest
{
    @Test
    public void testCreateTemplate()
    {
        TemplateImpl template = TemplateImpl.createTemplate( MockTemplates.getReassignSimpleTemplate() );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof ReassignSimpleTemplateImpl );
        assertValuesMatch( ( ReassignSimpleTemplateImpl ) template, "5.0" );
    }

    @Test
    public void testParseTemplate()
    {
        ReassignSimpleTemplateImpl template = new ReassignSimpleTemplateV5Impl( MockTemplates.getReassignSimpleTemplate() );
        assertValuesMatch( template, "5.0" );
    }

    private void assertValuesMatch( ReassignSimpleTemplateImpl template, String version )
    {
        Assert.assertNotNull( template );
        if ( "5.0".equals( version ) )
        {
            Assert.assertEquals( template.getApiKey(), "API-0000-0000-0000-0000" );
        }
        Assert.assertEquals( template.getAction(), PocTemplateImpl.Action.CREATE );
        Assert.assertEquals( template.getNetName(), "Net Name" );
        Assert.assertEquals( template.getIpAddress(), "121.143.100.000\\24" );
        Assert.assertEquals( template.getOriginAses().size(), 3 );
        Assert.assertTrue( template.getOriginAses().contains( "AS102" ) );
        Assert.assertTrue( template.getOriginAses().contains( "AS103" ) );
        Assert.assertTrue( template.getOriginAses().contains( "AS104" ) );
        Assert.assertEquals( template.getPrivateCustomer(), "Y" );
        Assert.assertEquals( template.getCustomerName(), "Jane Doe" );
        Assert.assertEquals( template.getAddress(), Arrays.asList( "123 Main St.", "Apt 100" ) );
        Assert.assertEquals( template.getCity(), "Arlington" );
        Assert.assertEquals( template.getStateProvince(), "VA" );
        Assert.assertEquals( template.getCountryCode(), "US" );
        Assert.assertEquals( template.getPostalCode(), "22203" );
        Assert.assertEquals( template.getPublicComments(), Arrays.asList( "Email me! I love spam!", "Line 2 of comments" ) );
    }
}
