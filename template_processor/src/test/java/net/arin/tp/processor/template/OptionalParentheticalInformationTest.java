package net.arin.tp.processor.template;

import net.arin.tp.processor.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

public class OptionalParentheticalInformationTest extends BaseTest
{
    @Test
    public void testReassignSimpleTemplateWithIncorrectParentheticalData()
    {
        TemplateImpl template = TemplateImpl.createTemplate( getReassignSimpleTemplateWithIncorrectParentheticalData() );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof ReassignSimpleTemplateImpl );
        assertValuesMatch( ( ReassignSimpleTemplateImpl ) template, "5.0" );
    }

    @Test
    public void testReassignSimpleTemplateWithMissingParentheticalData()
    {
        TemplateImpl template = TemplateImpl.createTemplate( getReassignSimpleTemplateWithMissingParentheticalData() );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof ReassignSimpleTemplateImpl );
        assertValuesMatch( ( ReassignSimpleTemplateImpl ) template, "5.0" );
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

    private String getReassignSimpleTemplateWithIncorrectParentheticalData()
    {
        return "Template: ARIN-REASSIGN-SIMPLE-5.0\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n" +
                "00. API Key: API-0000-0000-0000-0000\n" +
                "\n" +
                "01. Registration Action (Blah, blah or blah): N\n" +
                "02. Network Name: Net Name\n" +
                "03. IP Address and Prefix or Range: 121.143.100.000\\24\n" +
                "04. Origin AS: AS102, AS103, AS104\n" +
                "05. Private (Random): Yes\n" +
                "06. Customer Name: Jane Doe\n" +
                "07. Customer Address: 123 Main St.\n" +
                "07. Customer Address: Apt 100\n" +
                "08. Customer City: Arlington\n" +
                "09. Customer State/Province: VA\n" +
                "10. Customer Postal Code: 22203\n" +
                "11. Customer Country Code:US\n" +
                "12. Public Comments: Email me! I love spam!\n" +
                "12. Public Comments: Line 2 of comments\n" +
                "\n" +
                "END OF TEMPLATE";
    }

    private String getReassignSimpleTemplateWithMissingParentheticalData()
    {
        return "Template: ARIN-REASSIGN-SIMPLE-5.0\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n" +
                "00. API Key: API-0000-0000-0000-0000\n" +
                "\n" +
                "01. Registration Action: N\n" +
                "02. Network Name: Net Name\n" +
                "03. IP Address and Prefix or Range: 121.143.100.000\\24\n" +
                "04. Origin AS: AS102, AS103, AS104\n" +
                "05. Private: Yes\n" +
                "06. Customer Name: Jane Doe\n" +
                "07. Customer Address: 123 Main St.\n" +
                "07. Customer Address: Apt 100\n" +
                "08. Customer City: Arlington\n" +
                "09. Customer State/Province: VA\n" +
                "10. Customer Postal Code: 22203\n" +
                "11. Customer Country Code:US\n" +
                "12. Public Comments: Email me! I love spam!\n" +
                "12. Public Comments: Line 2 of comments\n" +
                "\n" +
                "END OF TEMPLATE";
    }
}
