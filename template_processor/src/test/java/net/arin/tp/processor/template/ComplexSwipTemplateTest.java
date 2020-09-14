package net.arin.tp.processor.template;

import net.arin.tp.processor.BaseTest;
import org.testng.Assert;

public class ComplexSwipTemplateTest extends BaseTest
{
    enum TemplateType
    {
        REASSIGN_DETAILED,
        REALLOCATE
    }

    enum TemplateVersion
    {
        FOUR,
        FIVE
    }

    String getComplexTemplate( TemplateType type, TemplateVersion version )
    {
        String name = null;

        switch ( type )
        {
            case REASSIGN_DETAILED:
                name = "ARIN-REASSIGN-DETAILED";
                break;
            case REALLOCATE:
                name = "ARIN-REALLOCATE";
                break;
        }

        switch ( version )
        {
            case FOUR:
                name += "-4.2";
                break;
            case FIVE:
                name += "-5.0";
                break;
        }

        String template = "Template: " + name + "\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n";

        if ( version == TemplateVersion.FIVE )
        {
            template += "00. API Key: API-0000-0000-0000-0000\n";
        }

        template += "\n" +
                "01. Downstream Org ID: testorg\n" +
                "** IF DOWNSTREAM ORG ID IS PROVIDED SKIP TO LINE 20.\n" +
                "\n" +
                "02. Org Name: Test Organization\n" +
                "03. Org Address: 123 Road Drive\n" +
                "04. Org City: Chantilly\n" +
                "05. Org State/Province: VA\n" +
                "06. Org Postal Code: 20152\n" +
                "07. Org Country Code: US\n" +
                "08. Org POC Handle: testorgpoc\n" +
                "** IF POC HANDLE IS PROVIDED SKIP TO LINE 20.\n" +
                "\n" +
                "09. Org POC Contact Type (P or R): PERSON\n" +
                "10. Org POC Last Name or Role Account: Reuben\n" +
                "11. Org POC First Name: Darth\n" +
                "12. Org POC Company Name: Waste Disposal\n" +
                "13. Org POC Address: 123 Place Street\n" +
                "14. Org POC City: Fairfax\n" +
                "15. Org POC State/Province: VA\n" +
                "16. Org POC Postal Code: 22031\n" +
                "17. Org POC Country Code: US\n" +
                "18. Org POC Office Phone Number: 111-222-1111\n" +
                "19. Org POC E-mail Address: dreuben@wasted.org\n" +
                "\n" +
                "** NETWORK SECTION\n" +
                "20. IP Address and Prefix or Range: 161.115.0.0/19\n" +
                "21. Network Name: Net Name\n" +
                "22. Origin AS: 1234\n" +
                "23. Hostname of DNS Reverse Mapping Nameserver:\n" +
                "23. Hostname of DNS Reverse Mapping Nameserver:\n" +
                "\n";

        if ( version == TemplateVersion.FOUR )
        {
            template += "** OPTIONAL RESOURCE CONTACT SECTION\n" +
                    "24. Net POC Type (T, AB, or N):\n" +
                    "\n" +
                    "25. Net POC Handle:\n" +
                    "** IF POC HANDLE IS PROVIDED SKIP TO LINE 37.\n" +
                    "\n" +
                    "26. Net POC Contact Type (P or R):\n" +
                    "27. Net POC Last Name or Role Account:\n" +
                    "28. Net POC First Name:\n" +
                    "29. Net POC Company Name:\n" +
                    "30. Net POC Address:\n" +
                    "30. Net POC Address:\n" +
                    "31. Net POC City:\n" +
                    "32. Net POC State/Province:\n" +
                    "33. Net POC Postal Code:\n" +
                    "34. Net POC Country Code:\n" +
                    "35. Net POC Office Phone Number:\n" +
                    "36. Net POC E-mail Address:\n" +
                    "\n";
        }

        template += "** OTHER OPTIONAL FIELDS\n" +
                "37. Public Comments: some public comments\n" +
                "38. Additional Information: some additional information\n" +
                "\n" +
                "END OF TEMPLATE";

        return template;
    }

    String getComplexTemplateIPVersion6( TemplateType type, TemplateVersion version )
    {
        String name = null;

        switch ( type )
        {
            case REASSIGN_DETAILED:
                name = "ARIN-IPv6-REASSIGN";
                break;
            case REALLOCATE:
                name = "ARIN-IPv6-REALLOCATE";
                break;
        }

        switch ( version )
        {
            case FOUR:
                name += "-4.2";
                break;
            case FIVE:
                name += "-5.0";
                break;
        }

        String template = "Template: " + name + "\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n";

        if ( version == TemplateVersion.FIVE )
        {
            template += "00. API Key: API-0000-0000-0000-0000\n";
        }

        template += "\n" +
                "01. Downstream Org ID: testorg\n" +
                "** IF DOWNSTREAM ORG ID IS PROVIDED SKIP TO LINE 20.\n" +
                "\n" +
                "02. Org Name: Test Organization\n" +
                "03. Org Address: 123 Road Drive\n" +
                "04. Org City: Chantilly\n" +
                "05. Org State/Province: VA\n" +
                "06. Org Postal Code: 20152\n" +
                "07. Org Country Code: US\n" +
                "08. Org POC Handle: testorgpoc\n" +
                "** IF POC HANDLE IS PROVIDED SKIP TO LINE 20.\n" +
                "\n" +
                "09. Org POC Contact Type (P or R): PERSON\n" +
                "10. Org POC Last Name or Role Account: Reuben\n" +
                "11. Org POC First Name: Darth\n" +
                "12. Org POC Company Name: Waste Disposal\n" +
                "13. Org POC Address: 123 Place Street\n" +
                "14. Org POC City: Fairfax\n" +
                "15. Org POC State/Province: VA\n" +
                "16. Org POC Postal Code: 22031\n" +
                "17. Org POC Country Code: US\n" +
                "18. Org POC Office Phone Number: 111-222-1111\n" +
                "19. Org POC E-mail Address: dreuben@wasted.org\n" +
                "\n" +
                "** NETWORK SECTION\n" +
                "20. IP Address and Prefix or Range: 2611:0033:0000:2404:0000:0000:0000:0000/62\n" +
                "21. Network Name: Net Name\n" +
                "22. Origin AS: 1234\n" +
                "23. Justification: This is the justification.\n" +
                "\n";

        if ( version == TemplateVersion.FOUR )
        {
            template += "** OPTIONAL RESOURCE CONTACT SECTION\n" +
                    "24. Net POC Type (T, AB, or N):\n" +
                    "\n" +
                    "25. Net POC Handle:\n" +
                    "** IF POC HANDLE IS PROVIDED SKIP TO LINE 37.\n" +
                    "\n" +
                    "26. Net POC Contact Type (P or R):\n" +
                    "27. Net POC Last Name or Role Account:\n" +
                    "28. Net POC First Name:\n" +
                    "29. Net POC Company Name:\n" +
                    "30. Net POC Address:\n" +
                    "30. Net POC Address:\n" +
                    "31. Net POC City:\n" +
                    "32. Net POC State/Province:\n" +
                    "33. Net POC Postal Code:\n" +
                    "34. Net POC Country Code:\n" +
                    "35. Net POC Office Phone Number:\n" +
                    "36. Net POC E-mail Address:\n" +
                    "\n";
        }

        template += "** OTHER OPTIONAL FIELDS\n" +
                "37. Public Comments: some public comments\n" +
                "38. Additional Information: some additional information\n" +
                "\n" +
                "END OF TEMPLATE";

        return template;
    }

    void assertValuesMatch( ComplexSwipTemplateImpl template )
    {
        Assert.assertNotNull( template );

        if ( template.getTemplateVersion().equals( "5.0" ) )
        {
            Assert.assertEquals( template.getApiKey(), "API-0000-0000-0000-0000" );
        }

        // Make sure the downstream org populates properly.
        Assert.assertEquals( template.getEmbeddedOrg().getOrgHandle(), "TESTORG" ); // Uppercased?
        Assert.assertEquals( template.getEmbeddedOrg().getLegalName(), "Test Organization" );
        Assert.assertEquals( template.getEmbeddedOrg().getAddress().get( 0 ), "123 Road Drive" );
        Assert.assertEquals( template.getEmbeddedOrg().getCity(), "Chantilly" );
        Assert.assertEquals( template.getEmbeddedOrg().getState(), "VA" );
        Assert.assertEquals( template.getEmbeddedOrg().getPostalCode(), "20152" );
        Assert.assertEquals( template.getEmbeddedOrg().getCountryCode(), "US" );
        Assert.assertEquals( template.getEmbeddedOrg().getAdminPocHandle(), "TESTORGPOC" ); // Uppercased?
        Assert.assertEquals( template.getEmbeddedOrg().getTechPocHandles().get( 0 ), "TESTORGPOC" ); // Uppercased?

        // Make sure the downstream org poc populates properly.
        Assert.assertEquals( template.getEmbeddedPoc().getPocContactType(), "PERSON" );
        Assert.assertEquals( template.getEmbeddedPoc().getLastName(), "Reuben" );
        Assert.assertEquals( template.getEmbeddedPoc().getFirstName(), "Darth" );
        Assert.assertEquals( template.getEmbeddedPoc().getCompanyName(), "Waste Disposal" );
        Assert.assertEquals( template.getEmbeddedPoc().getAddress().get( 0 ), "123 Place Street" );
        Assert.assertEquals( template.getEmbeddedPoc().getCity(), "Fairfax" );
        Assert.assertEquals( template.getEmbeddedPoc().getStateProvince(), "VA" );
        Assert.assertEquals( template.getEmbeddedPoc().getPostalCode(), "22031" );
        Assert.assertEquals( template.getEmbeddedPoc().getCountryCode(), "US" );
        Assert.assertEquals( template.getEmbeddedPoc().getOfficePhone().get( 0 ), "111-222-1111" );
        Assert.assertEquals( template.getEmbeddedPoc().getEmail().get( 0 ), "dreuben@wasted.org" );

        // Check the net section.
        Assert.assertEquals( template.getNetName(), "Net Name" );
        Assert.assertEquals( template.getOriginAses().get( 0 ), "1234" );
        Assert.assertEquals( template.getIpAddress(), "161.115.0.0/19" );

        // Optional fields.
        Assert.assertEquals( template.getPublicComments().get( 0 ), "some public comments" );
        Assert.assertEquals( template.getAdditionalInfo().get( 0 ), "some additional information" );
    }

    void assertValuesMatchV6Template( ComplexSwipTemplateImpl template )
    {
        Assert.assertNotNull( template );

        if ( template.getTemplateVersion().equals( "5.0" ) )
        {
            Assert.assertEquals( template.getApiKey(), "API-0000-0000-0000-0000" );
        }

        // Make sure the downstream org populates properly.
        Assert.assertEquals( template.getEmbeddedOrg().getOrgHandle(), "TESTORG" ); // Uppercased?
        Assert.assertEquals( template.getEmbeddedOrg().getLegalName(), "Test Organization" );
        Assert.assertEquals( template.getEmbeddedOrg().getAddress().get( 0 ), "123 Road Drive" );
        Assert.assertEquals( template.getEmbeddedOrg().getCity(), "Chantilly" );
        Assert.assertEquals( template.getEmbeddedOrg().getState(), "VA" );
        Assert.assertEquals( template.getEmbeddedOrg().getPostalCode(), "20152" );
        Assert.assertEquals( template.getEmbeddedOrg().getCountryCode(), "US" );
        Assert.assertEquals( template.getEmbeddedOrg().getAdminPocHandle(), "TESTORGPOC" ); // Uppercased?
        Assert.assertEquals( template.getEmbeddedOrg().getTechPocHandles().get( 0 ), "TESTORGPOC" ); // Uppercased?

        // Make sure the downstream org poc populates properly.
        Assert.assertEquals( template.getEmbeddedPoc().getPocContactType(), "PERSON" );
        Assert.assertEquals( template.getEmbeddedPoc().getLastName(), "Reuben" );
        Assert.assertEquals( template.getEmbeddedPoc().getFirstName(), "Darth" );
        Assert.assertEquals( template.getEmbeddedPoc().getCompanyName(), "Waste Disposal" );
        Assert.assertEquals( template.getEmbeddedPoc().getAddress().get( 0 ), "123 Place Street" );
        Assert.assertEquals( template.getEmbeddedPoc().getCity(), "Fairfax" );
        Assert.assertEquals( template.getEmbeddedPoc().getStateProvince(), "VA" );
        Assert.assertEquals( template.getEmbeddedPoc().getPostalCode(), "22031" );
        Assert.assertEquals( template.getEmbeddedPoc().getCountryCode(), "US" );
        Assert.assertEquals( template.getEmbeddedPoc().getOfficePhone().get( 0 ), "111-222-1111" );
        Assert.assertEquals( template.getEmbeddedPoc().getEmail().get( 0 ), "dreuben@wasted.org" );

        // Check the net section.
        Assert.assertEquals( template.getNetName(), "Net Name" );
        Assert.assertEquals( template.getOriginAses().get( 0 ), "1234" );
        Assert.assertEquals( template.getIpAddress(), "2611:0033:0000:2404:0000:0000:0000:0000/62" );
        Assert.assertEquals( template.getJustification().get( 0 ), "This is the justification." );

        // Optional fields.
        Assert.assertEquals( template.getPublicComments().get( 0 ), "some public comments" );
        Assert.assertEquals( template.getAdditionalInfo().get( 0 ), "some additional information" );
    }
}
