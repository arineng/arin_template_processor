package net.arin.tp.processor.transform;

import net.arin.tp.api.payload.OrgPayload;
import net.arin.tp.api.payload.TicketPayload;
import net.arin.tp.processor.BaseTest;
import net.arin.tp.processor.exception.AmbiguousFieldException;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.template.OrgTemplateImpl;
import net.arin.tp.processor.template.OrgTemplateV4Impl;
import net.arin.tp.processor.template.OrgTemplateV5Impl;
import net.arin.tp.processor.template.TemplateImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.mail.Message;

import static java.util.Arrays.asList;

public class OrgTransformerTest extends BaseTest
{
    @Test
    public void testCreatePayload()
    {
        OrgTemplateImpl template = new OrgTemplateV4Impl();

        template.setAction( TemplateImpl.Action.CREATE );
        template.setOrgHandle( "ORG-HANDLE" );
        template.setLegalName( "ACME" );
        template.setDba( "ACNE" );
        template.setTaxId( "42" );
        template.setAddress( asList( "123 Maple Ave", "Apt B" ) );
        template.setCity( "Arlington" );
        template.setState( "VA" );
        template.setPostalCode( "31337" );
        template.setCountryCode( "US" );
        template.setAdminPocHandle( "ADMIN" );
        template.setTechPocHandles( asList( "TECH1", "TECH2", "TECH3" ) );
        template.setAbusePocHandles( asList( "ABUSE1", "ABUSE2", "ABUSE3" ) );
        template.setNocPocHandles( asList( "NOCPOC1", "NOCPOC2", "NOCPOC3" ) );
        template.setReferralServer( "foobar" );
        template.getPublicComments().add( "HI!" );

        OrgPayload payload = OrgTransformer.createPayload( template );

        Assert.assertEquals( payload.getOrgHandle(), template.getOrgHandle() );
        Assert.assertEquals( payload.getTaxId(), template.getTaxId() );
        Assert.assertEquals( payload.getCity(), template.getCity() );
        Assert.assertEquals( payload.getPostalCode(), template.getPostalCode() );
        Assert.assertEquals( payload.getCountry().getAlpha2Code(), template.getCountryCode() );
        Assert.assertEquals( payload.getOrgName(), template.getLegalName() );
        Assert.assertEquals( payload.getDbaName(), template.getDba() );

        Assert.assertEquals( payload.getMultilineStreet().getText(), StringUtils.join( template.getAddress(), "\n" ) );

        Assert.assertEquals( payload.getStateProvince(), template.getState() );

        Assert.assertEquals( payload.getAdminPoc(), template.getAdminPocHandle() );

        // For each set, assert that the payload set has the same size as the known size of the template set. Then,
        // assert that both sets contain exactly the same elements by asserting that their disjunction is empty.
        Assert.assertEquals( payload.getTechPocs().size(), 3 );
        Assert.assertEquals( CollectionUtils.disjunction( payload.getTechPocs(), template.getTechPocHandles() ).size(), 0 );

        Assert.assertEquals( payload.getAbusePocs().size(), 3 );
        Assert.assertEquals( CollectionUtils.disjunction( payload.getAbusePocs(), template.getAbusePocHandles() ).size(), 0 );

        Assert.assertEquals( payload.getNocPocs().size(), 3 );
        Assert.assertEquals( CollectionUtils.disjunction( payload.getNocPocs(), template.getNocPocHandles() ).size(), 0 );

        Assert.assertEquals( payload.getOrgUrl(), "foobar" );

        Assert.assertEquals( payload.getMultilinePublicComments().getText(), StringUtils.join( template.getPublicComments(), "\n" ) );
    }

    @Test
    public void testCreateOrg() throws Exception
    {
        OrgTemplateImpl template = new OrgTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( TemplateImpl.Action.CREATE );
        template.setLegalName( "ACME" );
        template.setDba( "ACNE" );
        template.setTaxId( "42" );
        template.setAddress( asList( "123 Maple Ave", "Apt B" ) );
        template.setCity( "Arlington" );
        template.setState( "VA" );
        template.setPostalCode( "31337" );
        template.setCountryCode( "US" );
        template.setAdminPocHandle( "ADMIN" );
        template.setTechPocHandles( asList( "TECH1", "TECH2", "TECH3" ) );
        template.setAbusePocHandles( asList( "ABUSE1", "ABUSE2", "ABUSE3" ) );
        template.setNocPocHandles( asList( "NOCPOC1", "NOCPOC2", "NOCPOC3" ) );
        template.setReferralServer( "foobar" );
        template.getPublicComments().add( "HI!" );
        template.getAdditionalInfo().add( "See: Additional Info" );

        // Create the payload that will be returned.
        final TicketPayload ticketPayload = new TicketPayload();
        ticketPayload.setTicketNo( "TICKET-XYZ" );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                createResponse( HttpStatus.SC_OK, ticketPayload );
            }
        } );

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTemplate( template );

        route( templateMessage );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "TICKET-XYZ" ) );
    }

    @Test
    public void testModifyOrg() throws Exception
    {
        OrgTemplateImpl template = new OrgTemplateV4Impl();

        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( TemplateImpl.Action.MODIFY );
        template.setOrgHandle( "ORG-HANDLE" );
        template.setLegalName( "ACME" );
        template.setDba( "ACNE" );
        template.setTaxId( "42" );
        template.setAddress( asList( "123 Maple Ave", "Apt B" ) );
        template.setCity( "Arlington" );
        template.setState( "VA" );
        template.setPostalCode( "31337" );
        template.setCountryCode( "US" );
        template.setAdminPocHandle( "ADMIN" );
        template.setTechPocHandles( asList( "TECH1", "TECH2", "TECH3" ) );
        template.setAbusePocHandles( asList( "ABUSE1", "ABUSE2", "ABUSE3" ) );
        template.setNocPocHandles( asList( "NOCPOC1", "NOCPOC2", "NOCPOC3" ) );
        template.setReferralServer( "foobar" );
        template.getPublicComments().add( "HI!" );
        template.getAdditionalInfo().add( "See: Additional Info" );

        // This is the payload that the mocked "get" will return.
        final OrgPayload getPayload = OrgTransformer.createPayload( template );

        // Modify the template slightly before constructing the payload to be "put".
        template.setCity( "Ridgway" );
        final OrgPayload putPayload = OrgTransformer.modifyPayload( template, new OrgPayload() );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                createResponse( HttpStatus.SC_OK, getPayload );
                createResponse( HttpStatus.SC_OK, putPayload );
            }
        } );

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTemplate( template );

        route( templateMessage );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();

        Assert.assertTrue( content.contains( "ARIN has completed the processing of your template as requested" ) );
        Assert.assertTrue( content.contains( "http://whois.arin.net/rest/org/ORG-HANDLE" ) );
    }

    @Test
    public void testModifyOrgWithAmbiguousNocPoc()
    {
        OrgTemplateImpl template = new OrgTemplateV4Impl();

        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( TemplateImpl.Action.MODIFY );
        template.setOrgHandle( "ORG-HANDLE" );
        template.setLegalName( "ACME" );
        template.setDba( "ACNE" );
        template.setTaxId( "42" );
        template.setAddress( asList( "123 Maple Ave", "Apt B" ) );
        template.setCity( "Arlington" );
        template.setState( "VA" );
        template.setPostalCode( "31337" );
        template.setCountryCode( "US" );
        template.setAdminPocHandle( "ADMIN" );
        template.setTechPocHandles( asList( "TECH1", "TECH2", "TECH3" ) );
        template.setAbusePocHandles( asList( "ABUSE1", "ABUSE2", "ABUSE3" ) );
        template.setNocPocHandles( asList( "NOCPOC1", "NONE", "NOCPOC2", "NOCPOC3" ) );
        template.setReferralServer( "foobar" );
        template.getPublicComments().add( "HI!" );
        template.getAdditionalInfo().add( "See: Additional Info" );

        try
        {
            OrgTransformer.modifyPayload( template, new OrgPayload() );
            Assert.fail( "AmbiguousFieldException should have been thrown!" );
        }
        catch ( AmbiguousFieldException e )
        {
            Assert.assertTrue( e.getMessage().contains( OrgTemplateImpl.NOC_POC_HANDLE_LABEL ) );
        }
    }

    @Test
    public void testModifyOrgWithAmbiguousAbusePoc()
    {
        OrgTemplateImpl template = new OrgTemplateV4Impl();

        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( TemplateImpl.Action.MODIFY );
        template.setOrgHandle( "ORG-HANDLE" );
        template.setLegalName( "ACME" );
        template.setDba( "ACNE" );
        template.setTaxId( "42" );
        template.setAddress( asList( "123 Maple Ave", "Apt B" ) );
        template.setCity( "Arlington" );
        template.setState( "VA" );
        template.setPostalCode( "31337" );
        template.setCountryCode( "US" );
        template.setAdminPocHandle( "ADMIN" );
        template.setTechPocHandles( asList( "TECH1", "TECH2", "TECH3" ) );
        template.setAbusePocHandles( asList( "ABUSE1", "NONE", "ABUSE2", "ABUSE3" ) );
        template.setNocPocHandles( asList( "NOCPOC1", "NOCPOC2", "NOCPOC3" ) );
        template.setReferralServer( "foobar" );
        template.getPublicComments().add( "HI!" );
        template.getAdditionalInfo().add( "See: Additional Info" );

        try
        {
            OrgTransformer.modifyPayload( template, new OrgPayload() );
            Assert.fail( "AmbiguousFieldException should have been thrown!" );
        }
        catch ( AmbiguousFieldException e )
        {
            Assert.assertTrue( e.getMessage().contains( OrgTemplateImpl.ABUSE_POC_HANDLE_LABEL ) );
        }
    }

    @Test
    public void testModifyOrgWithAmbiguousTechPoc()
    {
        OrgTemplateImpl template = new OrgTemplateV4Impl();

        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( TemplateImpl.Action.MODIFY );
        template.setOrgHandle( "ORG-HANDLE" );
        template.setLegalName( "ACME" );
        template.setDba( "ACNE" );
        template.setTaxId( "42" );
        template.setAddress( asList( "123 Maple Ave", "Apt B" ) );
        template.setCity( "Arlington" );
        template.setState( "VA" );
        template.setPostalCode( "31337" );
        template.setCountryCode( "US" );
        template.setAdminPocHandle( "ADMIN" );
        template.setTechPocHandles( asList( "TECH1", "NONE", "TECH2", "TECH3" ) );
        template.setAbusePocHandles( asList( "ABUSE1", "ABUSE2", "ABUSE3" ) );
        template.setNocPocHandles( asList( "NOCPOC1", "NOCPOC2", "NOCPOC3" ) );
        template.setReferralServer( "foobar" );
        template.getPublicComments().add( "HI!" );
        template.getAdditionalInfo().add( "See: Additional Info" );

        try
        {
            OrgTransformer.modifyPayload( template, new OrgPayload() );
            Assert.fail( "AmbiguousFieldException should have been thrown!" );
        }
        catch ( AmbiguousFieldException e )
        {
            Assert.assertTrue( e.getMessage().contains( OrgTemplateImpl.TECH_POC_HANDLE_LABEL ) );
        }
    }

    @Test
    public void testModifyWithLowercaseStateName()
    {
        OrgTemplateImpl template = new OrgTemplateV4Impl();

        template.setState( "va" );

        OrgPayload putPayload = OrgTransformer.modifyPayload( template, new OrgPayload() );

        Assert.assertEquals( "VA", putPayload.getStateProvince() );
    }

    @Test
    public void testRemoveOrg() throws Exception
    {
        OrgTemplateImpl template = new OrgTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( TemplateImpl.Action.REMOVE );
        template.setOrgHandle( "ORG-HANDLE" );

        final OrgPayload payload = OrgTransformer.createPayload( template );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                createResponse( HttpStatus.SC_OK, payload );
            }
        } );

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTemplate( template );

        route( templateMessage );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "ARIN has completed the processing of your template as requested" ) );
        Assert.assertTrue( content.contains( "ORG-HANDLE will be removed shortly." ) );
    }

    @Test
    public void testCreateOrgV5Template() throws Exception
    {
        OrgTemplateImpl template = new OrgTemplateV5Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( TemplateImpl.Action.CREATE );
        template.setLegalName( "ACME" );
        template.setDba( "ACNE" );
        template.setTaxId( "42" );
        template.setAddress( asList( "123 Maple Ave", "Apt B" ) );
        template.setCity( "Arlington" );
        template.setState( "VA" );
        template.setPostalCode( "31337" );
        template.setCountryCode( "US" );
        template.setAdminPocHandle( "ADMIN" );
        template.setTechPocHandles( asList( "TECH1", "TECH2", "TECH3" ) );
        template.setAbusePocHandles( asList( "ABUSE1", "ABUSE2", "ABUSE3" ) );
        template.setNocPocHandles( asList( "NOCPOC1", "NOCPOC2", "NOCPOC3" ) );
        template.setReferralServer( "foobar" );
        template.getPublicComments().add( "HI!" );
        template.getAdditionalInfo().add( "See: Additional Info" );

        // Create the payload that will be returned.
        final TicketPayload ticketPayload = new TicketPayload();
        ticketPayload.setTicketNo( "TICKET-XYZ" );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                createResponse( HttpStatus.SC_OK, ticketPayload );
            }
        } );

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTemplate( template );

        route( templateMessage );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "TICKET-XYZ" ) );
    }
}
