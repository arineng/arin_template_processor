package net.arin.tp.processor.util;

import net.arin.tp.ipaddr.IPVersion;
import net.arin.tp.processor.template.TemplateImpl;

public class MockTemplates
{
    public static String getNetModTemplate( String templateVersion, IPVersion ipVersion, TemplateImpl.Action action )
    {
        StringBuilder template = new StringBuilder();

        if ( ipVersion.equals( IPVersion.IPV4 ) )
        {
            template.append( "Template: ARIN-NET-MOD" );
        }
        else if ( ipVersion.equals( IPVersion.IPV6 ) )
        {
            template.append( "Template: ARIN-IPv6-NET-MOD" );
        }

        if ( "4.1".equals( templateVersion ) )
        {
            template.append( "-4.1\n" +
                    "**  As of February 2007\n" +
                    "**  Detailed instructions are located below the template.\n" );
            template.append( getTemplateBody( ipVersion, templateVersion, action ) );
        }
        else if ( "5.0".equals( templateVersion ) )
        {
            template.append( "-5.0\n" +
                    "**  As of XXX 2010\n" +
                    "**  Detailed instructions are located below the template.\n" +
                    "\n" +
                    "00. API Key: API-0000-0000-0000-0000\n" );
            template.append( getTemplateBody( ipVersion, templateVersion, action ) );
        }

        return template.toString();
    }

    private static String getTemplateBody( IPVersion ipVersion, String templateVersion, TemplateImpl.Action action )
    {
        StringBuilder templateBody = new StringBuilder();
        if ( action.equals( TemplateImpl.Action.MODIFY ) )
        {
            templateBody.append( "01. Registration Action (M or R): M\n" );
        }
        else if ( action.equals( TemplateImpl.Action.REMOVE ) )
        {
            templateBody.append( "01. Registration Action (M or R): R\n" );
        }

        if ( ipVersion.equals( IPVersion.IPV4 ) )
        {
            templateBody.append( "02. IP Address and Prefix or Range: 10.0.0.0 - 10.0.0.255\n" );
        }
        else if ( ipVersion.equals( IPVersion.IPV6 ) )
        {
            templateBody.append( "02. IPv6 Address and Prefix: 2001:0100:0100::/48\n" );
        }

        if ( "4.1".equals( templateVersion ) )
        {
            templateBody.append( "03. Network Name: Net Name\n" +
                    "04. Origin AS: AS1\n" +
                    "05. Hostname of DNS Reverse Mapping Nameserver:\n" +
                    "06. Tech POC Handle: TechPoc\n" +
                    "07. Abuse POC Handle: AbusePoc\n" +
                    "08. NOC POC Handle: NocPoc\n" +
                    "09. Public Comments: These are comments for the public!\n" +
                    "10. Additional Information: This is some additional information.\n" +
                    "\n" +
                    "END OF TEMPLATE" );
        }
        else if ( "5.0".equals( templateVersion ) )
        {
            templateBody.append( "03. Network Name: Net Name\n" +
                    "04. Origin AS: AS1\n" +
                    "05. Tech POC Handle: TechPoc\n" +
                    "06. Abuse POC Handle: AbusePoc\n" +
                    "07. NOC POC Handle: NocPoc\n" +
                    "08. Public Comments: These are comments for the public!\n" +
                    "\n" +
                    "END OF TEMPLATE" );
        }

        return templateBody.toString();
    }

    public static String getV4OrgTemplate()
    {
        return getOrgTemplate( "4.0", getDefaultTemplateBody() );
    }

    public static String getV4OrgTemplate( String body )
    {
        return getOrgTemplate( "4.0", body );
    }

    public static String getV5OrgTemplate()
    {
        return getOrgTemplate( "5.0", getDefaultTemplateBody() );
    }

    private static String getOrgTemplate( String version, String body )
    {
        StringBuilder templateBody = new StringBuilder();

        if ( "4.0".equals( version ) )
        {
            templateBody.append( "Template: ARIN-ORG-4.0\n" );
        }
        else if ( "5.0".equals( version ) )
        {
            templateBody.append( "Template: ARIN-ORG-5.0\n" );
        }

        templateBody.append( "** As of July 2006\n" +
                "** Detailed instructions are located below the template.\n" );

        if ( "5.0".equals( version ) )
        {
            // Version 5 has API Key (that's the only difference save for the version number itself).
            templateBody.append( "00. API Key: API-0000-0000-0000-0000\n" );
        }

        templateBody.append( body );

        return templateBody.toString();
    }

    private static String getDefaultTemplateBody()
    {
        return "01. Registration Action (N,M, or R): N\n" +
                "\n" +
                "** IF REQUESTING A NEW ORG ID, LEAVE FIELD 02. BLANK\n" +
                "02. Existing OrgID:\n" +

                "03. Organization's Legal Name: ACME\n" +
                "04. Organization's D/B/A: ACNE\n" +
                "05. Business Tax ID Number (DO NOT LIST SSN): 42\n" +
                "06. Org Address: 1212 Yellow Brick Road\n" +
                "06. Org Address: Emerald Tower\n" +
                "07. Org City: Emerald City\n" +
                "08. Org State/Province: OZ\n" +
                "09. Org Postal Code: 31337\n" +
                "10. Org Country Code: US\n" +
                "11. Admin POC Handle: ADMIN\n" +
                "12. Tech POC Handle: TECH1\n" +
                "12. Tech POC Handle: TECH2\n" +

                "** OPTIONAL POC HANDLES\n" +
                "13. Abuse POC Handle: ABUSE1\n" +
                "13. Abuse POC Handle: ABUSE2\n" +
                "14. NOC POC Handle: NOCPOC1\n" +
                "14. NOC POC Handle: NOCPOC2\n" +

                "** OTHER OPTIONAL FIELDS\n" +
                "15. Referral Server: rwhois://www.foo.com\n" +
                "16. Public Comments: This form is too long!\n" +
                "17. Additional Information: I like avocados (esp. when they're RIPE)\n" +
                "This is a second line of additional information.\n" +
                "And another line with a list: one, two, three.  That could look like a label.\n" +

                "END OF TEMPLATE";
    }

    public static String getTemplateBodyWithUnlabelledMultilineCity()
    {
        return "01. Registration Action (N,M, or R): N\n" +
                "\n" +
                "** IF REQUESTING A NEW ORG ID, LEAVE FIELD 02. BLANK\n" +
                "02. Existing OrgID:\n" +

                "03. Organization's Legal Name: ACME\n" +
                "04. Organization's D/B/A: ACNE\n" +
                "05. Business Tax ID Number (DO NOT LIST SSN): 42\n" +
                "06. Org Address: 1212 Yellow Brick Road\n" +
                "06. Org Address: Emerald Tower\n" +
                "07. Org City: Emerald City\n" +
                "City of Emeralds\n" +
                "08. Org State/Province: OZ\n" +
                "09. Org Postal Code: 31337\n" +
                "10. Org Country Code: US\n" +
                "11. Admin POC Handle: ADMIN\n" +
                "12. Tech POC Handle: TECH1\n" +
                "12. Tech POC Handle: TECH2\n" +

                "** OPTIONAL POC HANDLES\n" +
                "13. Abuse POC Handle: ABUSE1\n" +
                "13. Abuse POC Handle: ABUSE2\n" +
                "14. NOC POC Handle: NOCPOC1\n" +
                "14. NOC POC Handle: NOCPOC2\n" +

                "** OTHER OPTIONAL FIELDS\n" +
                "15. Referral Server: rwhois://www.foo.com\n" +
                "16. Public Comments: This form is too long!\n" +
                "17. Additional Information: I like avocados (esp. when they're RIPE)\n" +
                "This is a second line of additional information.\n" +
                "And another line with a list: one, two, three.  That could look like a label.\n" +

                "END OF TEMPLATE";
    }

    public static String getTemplateBodyWithLabelledMultilineCity()
    {
        return "01. Registration Action (N,M, or R): N\n" +
                "\n" +
                "** IF REQUESTING A NEW ORG ID, LEAVE FIELD 02. BLANK\n" +
                "02. Existing OrgID:\n" +

                "03. Organization's Legal Name: ACME\n" +
                "04. Organization's D/B/A: ACNE\n" +
                "05. Business Tax ID Number (DO NOT LIST SSN): 42\n" +
                "06. Org Address: 1212 Yellow Brick Road\n" +
                "06. Org Address: Emerald Tower\n" +
                "07. Org City: Emerald City\n" +
                "07. Org City: City of Emeralds\n" +
                "08. Org State/Province: OZ\n" +
                "09. Org Postal Code: 31337\n" +
                "10. Org Country Code: US\n" +
                "11. Admin POC Handle: ADMIN\n" +
                "12. Tech POC Handle: TECH1\n" +
                "12. Tech POC Handle: TECH2\n" +

                "** OPTIONAL POC HANDLES\n" +
                "13. Abuse POC Handle: ABUSE1\n" +
                "13. Abuse POC Handle: ABUSE2\n" +
                "14. NOC POC Handle: NOCPOC1\n" +
                "14. NOC POC Handle: NOCPOC2\n" +

                "** OTHER OPTIONAL FIELDS\n" +
                "15. Referral Server: rwhois://www.foo.com\n" +
                "16. Public Comments: This form is too long!\n" +
                "17. Additional Information: I like avocados (esp. when they're RIPE)\n" +
                "This is a second line of additional information.\n" +
                "And another line with a list: one, two, three.  That could look like a label.\n" +

                "END OF TEMPLATE";
    }

    public static String getTemplateBodyWithMixedMultilinePublicComments()
    {
        return "01. Registration Action (N,M, or R): N\n" +
                "\n" +
                "** IF REQUESTING A NEW ORG ID, LEAVE FIELD 02. BLANK\n" +
                "02. Existing OrgID:\n" +

                "03. Organization's Legal Name: ACME\n" +
                "04. Organization's D/B/A: ACNE\n" +
                "05. Business Tax ID Number (DO NOT LIST SSN): 42\n" +
                "06. Org Address: 1212 Yellow Brick Road\n" +
                "06. Org Address: Emerald Tower\n" +
                "07. Org City: Emerald City\n" +
                "08. Org State/Province: OZ\n" +
                "09. Org Postal Code: 31337\n" +
                "10. Org Country Code: US\n" +
                "11. Admin POC Handle: ADMIN\n" +
                "12. Tech POC Handle: TECH1\n" +
                "12. Tech POC Handle: TECH2\n" +

                "** OPTIONAL POC HANDLES\n" +
                "13. Abuse POC Handle: ABUSE1\n" +
                "13. Abuse POC Handle: ABUSE2\n" +
                "14. NOC POC Handle: NOCPOC1\n" +
                "14. NOC POC Handle: NOCPOC2\n" +

                "** OTHER OPTIONAL FIELDS\n" +
                "15. Referral Server: rwhois://www.foo.com\n" +

                "16. Public Comments: This form is too long!\n" +
                "I'd rather be fishing\n" +
                "16. Public Comments: Actually, I'd rather be playing video games\n" +

                "17. Additional Information: I like avocados (esp. when they're RIPE)\n" +
                "This is a second line of additional information.\n" +
                "And another line with a list: one, two, three.  That could look like a label.\n" +

                "END OF TEMPLATE";
    }

    public static String getReassignSimpleTemplate()
    {
        return "Template: ARIN-REASSIGN-SIMPLE-5.0\n" +
                "**  As of July 2006\n" +
                "**  Detailed instructions are located below the template.\n" +
                "00. API Key: API-0000-0000-0000-0000\n" +
                "\n" +
                "01. Registration Action (N,M, or R): N\n" +
                "02. Network Name: Net Name\n" +
                "03. IP Address and Prefix or Range: 121.143.100.000\\24\n" +
                "04. Origin AS: AS102, AS103, AS104\n" +
                "05. Private (Yes or No): Yes\n" +
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
