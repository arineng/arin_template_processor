package net.arin.tp.processor.template;

import net.arin.tp.processor.BaseTest;
import net.arin.tp.processor.exception.TemplateFieldLimitExceededException;
import net.arin.tp.processor.util.MockTemplates;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import static java.util.Arrays.asList;

public class OrgTemplateTest extends BaseTest
{
    private static Logger log = LoggerFactory.getLogger( OrgTemplateTest.class );

    @Test
    public void testCreateV4Template()
    {
        TemplateImpl template = TemplateImpl.createTemplate( MockTemplates.getV4OrgTemplate() );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof OrgTemplateImpl );
        assertValuesMatch( ( OrgTemplateImpl ) template, "4.0" );
    }

    @Test
    public void testCreateV5Template()
    {
        TemplateImpl template = TemplateImpl.createTemplate( MockTemplates.getV5OrgTemplate() );
        Assert.assertNotNull( template );
        Assert.assertTrue( template instanceof OrgTemplateImpl );
        assertValuesMatch( ( OrgTemplateImpl ) template, "5.0" );
    }

    @Test
    public void testParseV4Template()
    {
        OrgTemplateImpl template = new OrgTemplateV4Impl( MockTemplates.getV4OrgTemplate() );
        assertValuesMatch( template, "4.0" );
    }

    @Test
    public void testParseV5Template()
    {
        OrgTemplateImpl template = new OrgTemplateV5Impl( MockTemplates.getV5OrgTemplate() );
        assertValuesMatch( template, "5.0" );
    }

    @Test
    public void testSingleLineFieldsCannotSpanMultipleLinesWithoutLabels()
    {
        try
        {
            // We're testing the Template class (that's where this error will be thrown). Testing Org V4 templates is
            // good enough. No need to test V5 too.
            new OrgTemplateV4Impl( MockTemplates.getV4OrgTemplate( MockTemplates.getTemplateBodyWithUnlabelledMultilineCity() ) );
            Assert.fail( "OrgTemplateV4: Single line fields cannot span multiple lines (even if unlabeled)." );
        }
        catch ( TemplateFieldLimitExceededException e )
        {
            log.info( "Single line fields cannot have multiple values", e );
            log.info( "================== That exception expected! ====================" );
            log.info( "OrgTemplateV4 correctly handling single line fields that span multiple lines (without labels) by throwing exception" );
        }
    }

    @Test
    public void testSingleLineFieldsCannotSpanMultipleLinesWithLabels()
    {
        try
        {
            new OrgTemplateV4Impl( MockTemplates.getV4OrgTemplate( MockTemplates.getTemplateBodyWithLabelledMultilineCity() ) );
            Assert.fail( "OrgTemplateV4: Single line fields cannot span multiple lines." );
        }
        catch ( TemplateFieldLimitExceededException e )
        {
            log.info( "Single line fields cannot have multiple values", e );
            log.info( "================== That exception expected! ====================" );
            log.info( "OrgTemplateV4 correctly handling single line fields that span multiple lines (with labels) by throwing exception" );
        }
    }

    @Test
    public void testMixedMultilinePublicComments()
    {
        OrgTemplateImpl v4template = new OrgTemplateV4Impl( MockTemplates.getTemplateBodyWithMixedMultilinePublicComments() );
        OrgTemplateImpl v5template = new OrgTemplateV5Impl( MockTemplates.getTemplateBodyWithMixedMultilinePublicComments() );

        Assert.assertEquals( normalizeMultilineTemplateProperty( v4template.getPublicComments() ).size(), 3 );
        Assert.assertEquals( normalizeMultilineTemplateProperty( v5template.getPublicComments() ).size(), 3 );
    }

    private void assertValuesMatch( OrgTemplateImpl template, String version )
    {
        if ( "5.0".equals( version ) )
        {
            Assert.assertEquals( template.getApiKey(), "API-0000-0000-0000-0000" );
        }

        Assert.assertNotNull( template );
        Assert.assertEquals( template.getAction(), TemplateImpl.Action.CREATE );
        Assert.assertNull( template.getOrgHandle() );
        Assert.assertEquals( template.getLegalName(), "ACME" );
        Assert.assertEquals( template.getDba(), "ACNE" );
        Assert.assertEquals( template.getTaxId(), "42" );
        Assert.assertEquals( template.getAddress().get( 0 ), "1212 Yellow Brick Road" );
        Assert.assertEquals( template.getAddress().get( 1 ), "Emerald Tower" );
        Assert.assertEquals( template.getCity(), "Emerald City" );
        Assert.assertEquals( template.getState(), "OZ" );
        Assert.assertEquals( template.getPostalCode(), "31337" );
        Assert.assertEquals( template.getCountryCode(), "US" );
        Assert.assertEquals( template.getAdminPocHandle(), "ADMIN" );

        Assert.assertEquals(
                CollectionUtils.disjunction( template.getTechPocHandles(), asList( "TECH1", "TECH2" ) ).size(), 0 );

        Assert.assertEquals(
                CollectionUtils.disjunction( template.getAbusePocHandles(), asList( "ABUSE1", "ABUSE2" ) ).size(), 0 );

        Assert.assertEquals(
                CollectionUtils.disjunction( template.getNocPocHandles(), asList( "NOCPOC1", "NOCPOC2" ) ).size(), 0 );

        Assert.assertEquals( template.getReferralServer(), "rwhois://www.foo.com" );
        Assert.assertEquals( StringUtils.join( template.getPublicComments(), "\n" ), "This form is too long!" );
        Assert.assertEquals( StringUtils.join( template.getAdditionalInfo(), "\n" ), "I like avocados (esp. when they're RIPE)\nThis is a second line of additional information.\n" +
                "And another line with a list: one, two, three.  That could look like a label." );
    }
}
