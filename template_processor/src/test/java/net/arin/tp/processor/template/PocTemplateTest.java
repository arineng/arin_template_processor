package net.arin.tp.processor.template;

import net.arin.tp.processor.BaseTest;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedList;

public class PocTemplateTest extends BaseTest
{
    @Test
    public void testCreateTemplate()
    {
        TemplateImpl template = TemplateImpl.createTemplate( getPocTemplate() );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof PocTemplateImpl );
        assertValuesMatch( ( PocTemplateImpl ) template, "5.0" );
    }

    @Test
    public void testParseTemplate()
    {
        PocTemplateImpl template = new PocTemplateV5Impl( getPocTemplate() );
        assertValuesMatch( template, "5.0" );
        Assert.assertEquals( template.getAddress().size(), 1 );
        Assert.assertEquals( template.getAddress().get( 0 ), "123 Parkway Drive" );
    }

    @Test
    public void testParseTemplateWithSpaceyFields()
    {
        PocTemplateImpl template = new PocTemplateV5Impl( getPocTemplateWithSpaceyLabels() );
        assertValuesMatch( template, "5.0" );
        Assert.assertEquals( template.getAddress().size(), 1 );
        Assert.assertEquals( template.getAddress().get( 0 ), "123 Parkway Drive" );
    }

    @Test
    public void testParseTemplateWithMultipleOfficePhoneNumbers()
    {
        PocTemplateImpl template = new PocTemplateV4Impl( getPocTemplateMultipleOfficePhones() );
        Assert.assertEquals( template.getOfficePhone().size(), 2 );
        Assert.assertEquals( template.getOfficePhoneExt().size(), 2 );
    }

    /**
     * This test ensures that PERSON, P, ROLE, and R get mapped to the appropriate PocPayload.ContactType.
     */
    @Test
    public void testParseTemplateWithPersonAndRole()
    {
        PocTemplateImpl template = new PocTemplateV4Impl( getPocTemplateROLE() );
        Assert.assertEquals( template.getPocContactType(), "ROLE" );

        template = new PocTemplateV4Impl( getPocTemplatePERSON() );
        Assert.assertEquals( template.getPocContactType(), "PERSON" );
    }

    @Test
    public void testParseTemplateWithWhiteSpace()
    {
        PocTemplateImpl template = new PocTemplateV5Impl( getPocTemplateExcessWhiteSpaceContactType() );
        assertValuesMatch( template, "5.0" );
        Assert.assertEquals( template.getAddress().size(), 1 );
        Assert.assertEquals( template.getAddress().get( 0 ), "123 Parkway Drive" );
    }

    @Test
    public void testParseTemplateWithMixedMultilinePublicCommentsAndAdditionalInformation()
    {
        PocTemplateImpl v4template = new PocTemplateV4Impl( getPocTemplateWithMixedMultilinePublicComments() );
        PocTemplateImpl v5template = new PocTemplateV5Impl( getPocTemplateWithMixedMultilinePublicComments() );

        Assert.assertEquals( normalizeMultilineTemplateProperty( v4template.getPublicComments() ).size(), 3 );
        Assert.assertEquals( normalizeMultilineTemplateProperty( v5template.getPublicComments() ).size(), 3 );
    }

    private void assertValuesMatch( PocTemplateImpl template, String version )
    {
        Assert.assertNotNull( template );
        if ( "5.0".equals( version ) )
        {
            Assert.assertEquals( template.getApiKey(), "API-0000-0000-0000-0000" );
        }
        Assert.assertEquals( template.getAction(), PocTemplateImpl.Action.CREATE );
        Assert.assertNull( template.getPocHandle() );
        Assert.assertEquals( template.getPocContactType(), "P" );
        Assert.assertEquals( template.getLastName(), "Kostner" );
        Assert.assertEquals( template.getFirstName(), "Daryl" );
        Assert.assertEquals( template.getMiddleName(), "Middle" );
        Assert.assertEquals( template.getCompanyName(), "Italy" );
        Assert.assertEquals( template.getCity(), "Fairless Hills" );
        Assert.assertEquals( template.getStateProvince(), "PA" );
        Assert.assertEquals( template.getCountryCode(), "US" );
        Assert.assertEquals( template.getOfficePhone().size(), template.getOfficePhoneExt().size() );
        Assert.assertEquals( template.getOfficePhone(), new LinkedList<>( Arrays.asList( "+1-333-444-9999" ) ) );
        Assert.assertEquals( template.getOfficePhoneExt(), new LinkedList<>( Arrays.asList( "" ) ) );
        Assert.assertEquals( template.getEmail().size(), 1 );
        Assert.assertEquals( template.getEmail().get( 0 ), "ckostner@skating.com" );
        Assert.assertEquals( template.getMobilePhone(), new LinkedList<>( Arrays.asList( "+1-222-777-5555" ) ) );
        Assert.assertEquals( template.getFaxPhone(), new LinkedList<String>() );
        Assert.assertEquals( StringUtils.join( template.getPublicComments(), "\n" ), "Email me! I love spam!" );
        Assert.assertNull( template.getAdditionalInfo() );
    }

    public static String getPocTemplate()
    {
        return "Template: ARIN-POC-5.0\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n" +
                "00. API Key: API-0000-0000-0000-0000\n" +
                "\n" +
                "01. Registration Action (N,M, or R): N\n" +
                "\n" +
                "02. Existing POC Handle:\n" +
                "** Skip field 02. if generating a new handle\n" +
                "\n" +
                "03. Contact Type (P or R): P\n" +
                "04. Last Name or Role Account: Kostner\n" +
                "05. First Name: Daryl\n" +
                "06. Middle Name: Middle\n" +
                "07. Company Name: Italy\n" +
                "08. Address: 123 Parkway Drive\n" +
                "09. City: Fairless Hills\n" +
                "10. State/Province: PA\n" +
                "11. Postal Code: 19030\n" +
                "12. Country Code: US\n" +
                "13. Office Phone Number: +1-333-444-9999\n" +
                "14. Office Phone Number Extension: \n" +
                "15. E-mail Address: ckostner@skating.com\n" +
                "\n" +
                "** OPTIONAL PHONE NUMBERS\n" +
                "16. Mobile: +1-222-777-5555\n" +
                "17. Fax: \n" +
                "\n" +
                "** OTHER OPTIONAL FIELDS\n" +
                "18. Public Comments: Email me! I love spam!\n" +
                "19. Additional Information:\n" +
                "\n" +
                "END OF TEMPLATE";
    }

    private String getPocTemplateWithSpaceyLabels()
    {
        return "Template: ARIN-POC-5.0\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n" +
                "00. API    Key   : API-0000-0000-0000-0000\n" +
                "\n" +
                "01. Registration    Action (  N  ,  M  , or R  ) : N\n" +
                "\n" +
                "02. Existing POC   Handle:\n" +
                "** Skip field 02. if generating a new handle\n" +
                "\n" +
                "03. Contact Type ( P or R): P\n" +
                "04. Last Name or Role Account : Kostner\n" +
                "05. First Name : Daryl\n" +
                "06. Middle   Name: Middle\n" +
                "07. Company Name: Italy\n" +
                "08. Address  : 123 Parkway Drive\n" +
                "09. City  : Fairless Hills\n" +
                "10. State / Province: PA\n" +
                "11. Postal   Code: 19030\n" +
                "12. Country Code  : US\n" +
                "13. Office Phone Number  : +1-333-444-9999\n" +
                "14. Office Phone Number Extension: \n" +
                "15. E-mail Address  : ckostner@skating.com\n" +
                "\n" +
                "** OPTIONAL PHONE NUMBERS\n" +
                "16. Mobile   : +1-222-777-5555\n" +
                "17. Fax: \n" +
                "\n" +
                "** OTHER OPTIONAL FIELDS\n" +
                "18. Public Comments   : Email me! I love spam!\n" +
                "19.     Additional Information:\n" +
                "\n" +
                "END OF TEMPLATE";
    }

    private String getPocTemplateROLE()
    {
        return "Template: ARIN-POC-5.0\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n" +
                "00. API Key: API-0000-0000-0000-0000\n" +
                "\n" +
                "01. Registration Action (N,M, or R): N\n" +
                "\n" +
                "02. Existing POC Handle:\n" +
                "** Skip field 02. if generating a new handle\n" +
                "\n" +
                "03. Contact Type (P or R): ROLE\n" +
                "04. Last Name or Role Account: Kostner\n" +
                "05. First Name: Daryl\n" +
                "06. Middle Name: Middle\n" +
                "07. Company Name: Italy\n" +
                "08. Address: 123 Parkway Drive\n" +
                "09. City: Fairless Hills\n" +
                "10. State/Province: PA\n" +
                "11. Postal Code: 19030\n" +
                "12. Country Code: US\n" +
                "13. Office Phone Number: +1-333-444-9999\n" +
                "14. Office Phone Number Extension: \n" +
                "15. E-mail Address: ckostner@skating.com\n" +
                "\n" +
                "** OPTIONAL PHONE NUMBERS\n" +
                "16. Mobile: +1-222-777-5555\n" +
                "17. Fax: \n" +
                "\n" +
                "** OTHER OPTIONAL FIELDS\n" +
                "18. Public Comments: Email me! I love spam!\n" +
                "19. Additional Information:\n" +
                "\n" +
                "END OF TEMPLATE";
    }

    private String getPocTemplatePERSON()
    {
        return "Template: ARIN-POC-5.0\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n" +
                "00. API Key: API-0000-0000-0000-0000\n" +
                "\n" +
                "01. Registration Action (N,M, or R): N\n" +
                "\n" +
                "02. Existing POC Handle:\n" +
                "** Skip field 02. if generating a new handle\n" +
                "\n" +
                "03. Contact Type (P or R): PERSON\n" +
                "04. Last Name or Role Account: Kostner\n" +
                "05. First Name: Daryl\n" +
                "06. Middle Name: Middle\n" +
                "07. Company Name: Italy\n" +
                "08. Address: 123 Parkway Drive\n" +
                "09. City: Fairless Hills\n" +
                "10. State/Province: PA\n" +
                "11. Postal Code: 19030\n" +
                "12. Country Code: US\n" +
                "13. Office Phone Number: +1-333-444-9999\n" +
                "14. Office Phone Number Extension: \n" +
                "15. E-mail Address: ckostner@skating.com\n" +
                "\n" +
                "** OPTIONAL PHONE NUMBERS\n" +
                "16. Mobile: +1-222-777-5555\n" +
                "17. Fax: \n" +
                "\n" +
                "** OTHER OPTIONAL FIELDS\n" +
                "18. Public Comments: Email me! I love spam!\n" +
                "19. Additional Information:\n" +
                "\n" +
                "END OF TEMPLATE";
    }

    private String getPocTemplateExcessWhiteSpaceContactType()
    {
        return "Template: ARIN-POC-5.0\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n" +
                "00. API Key: API-0000-0000-0000-0000\n" +
                "\n" +
                "01. Registration Action (N,M, or R): N\n" +
                "\n" +
                "02. Existing POC Handle:\n" +
                "** Skip field 02. if generating a new handle\n" +
                "\n" +
                "03. Contact Type (P or R): P  \n" +
                "04. Last Name or Role Account: Kostner\n" +
                "05. First Name: Daryl\n" +
                "06. Middle Name: Middle\n" +
                "07. Company Name: Italy\n" +
                "08. Address: 123 Parkway Drive\n" +
                "09. City: Fairless Hills\n" +
                "10. State/Province: PA\n" +
                "11. Postal Code: 19030\n" +
                "12. Country Code: US\n" +
                "13. Office Phone Number: +1-333-444-9999\n" +
                "14. Office Phone Number Extension: \n" +
                "15. E-mail Address: ckostner@skating.com\n" +
                "\n" +
                "** OPTIONAL PHONE NUMBERS\n" +
                "16. Mobile: +1-222-777-5555\n" +
                "17. Fax: \n" +
                "\n" +
                "** OTHER OPTIONAL FIELDS\n" +
                "18. Public Comments: Email me! I love spam!\n" +
                "19. Additional Information:\n" +
                "\n" +
                "END OF TEMPLATE";
    }

    private String getPocTemplateMultipleOfficePhones()
    {
        return "Template: ARIN-POC-5.0\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n" +
                "00. API Key: API-0000-0000-0000-0000\n" +
                "\n" +
                "01. Registration Action (N,M, or R): N\n" +
                "\n" +
                "02. Existing POC Handle:\n" +
                "** Skip field 02. if generating a new handle\n" +
                "\n" +
                "03. Contact Type (P or R): ROLE\n" +
                "04. Last Name or Role Account: Kostner\n" +
                "05. First Name: Daryl\n" +
                "06. Middle Name: Middle\n" +
                "07. Company Name: Italy\n" +
                "08. Address: 123 Parkway Drive\n" +
                "09. City: Fairless Hills\n" +
                "10. State/Province: PA\n" +
                "11. Postal Code: 19030\n" +
                "12. Country Code: US\n" +
                "13. Office Phone Number: +1-333-444-9999\n" +
                "14. Office Phone Number Extension: \n" +
                "13. Office Phone Number: +1-222-777-0000\n" +
                "14. Office Phone Number Extension: 47\n" +
                "15. E-mail Address: ckostner@skating.com\n" +
                "\n" +
                "** OPTIONAL PHONE NUMBERS\n" +
                "16. Mobile: +1-111-222-5555\n" +
                "17. Fax: \n" +
                "\n" +
                "** OTHER OPTIONAL FIELDS\n" +
                "18. Public Comments: Email me! I love spam!\n" +
                "19. Additional Information:\n" +
                "\n" +
                "END OF TEMPLATE";
    }

    private String getPocTemplateWithMixedMultilinePublicComments()
    {
        return "Template: ARIN-POC-5.0\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n" +
                "00. API Key: API-0000-0000-0000-0000\n" +
                "\n" +
                "01. Registration Action (N,M, or R): N\n" +
                "\n" +
                "02. Existing POC Handle:\n" +
                "** Skip field 02. if generating a new handle\n" +
                "\n" +
                "03. Contact Type (P or R): ROLE\n" +
                "04. Last Name or Role Account: Kostner\n" +
                "05. First Name: Daryl\n" +
                "06. Middle Name: Middle\n" +
                "07. Company Name: Italy\n" +
                "08. Address: 123 Parkway Drive\n" +
                "Suite 200\n" +
                "09. City: Fairless Hills\n" +
                "10. State/Province: PA\n" +
                "11. Postal Code: 19030\n" +
                "12. Country Code: US\n" +
                "13. Office Phone Number: +1-345-678-0000\n" +
                "14. Office Phone Number Extension: 47\n" +
                "15. E-mail Address: ckostner@skating.com\n" +
                "\n" +
                "** OPTIONAL PHONE NUMBERS\n" +
                "16. Mobile: +1-111-222-5555\n" +
                "17. Fax: \n" +
                "\n" +
                "** OTHER OPTIONAL FIELDS\n" +
                "18. Public Comments: Email me! \n" +
                "I love spam!\n" +
                "18. Public Comments: Just Kidding, I don't love spam (well, SPAM the meat product is okay)\n" +
                "19. Additional Information: Here is\n" +
                "Some \n" +
                "19. Additional Information: Additonal Info \n" +
                "\n" +
                "END OF TEMPLATE";
    }
}
