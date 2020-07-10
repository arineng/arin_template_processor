package net.arin.tp.processor.transform;

import net.arin.tp.api.payload.ErrorPayload;
import net.arin.tp.api.payload.PhoneTypePayload;
import net.arin.tp.api.payload.PocPayload;
import net.arin.tp.processor.BaseTest;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.template.PocTemplateImpl;
import net.arin.tp.processor.template.PocTemplateV4Impl;
import net.arin.tp.processor.template.PocTemplateV5Impl;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.mail.Message;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PocTransformerTest extends BaseTest
{
    @Test
    public void testCreatePayload()
    {
        PocTemplateImpl template = new PocTemplateV4Impl();
        template.setPocHandle( "HK31-ARIN" );
        template.setLastName( "Kerry" );
        template.setFirstName( "Harry" );
        template.setMiddleName( "Middle" );
        template.setCompanyName( "USPSRSVP" );
        template.getAddress().add( "923 Orly Yarly" );
        template.getAddress().add( "Suite 202" );
        template.setCity( "Anaheim" );
        template.setStateProvince( "CA" );
        template.setPostalCode( "92801" );
        template.setCountryCode( "US" );
        template.setOfficePhone( new LinkedList<>( List.of( "+1-714-523-8888" ) ) );
        template.setOfficePhoneExt( new LinkedList<>( List.of( "" ) ) );
        template.setEmail( new LinkedList<>( List.of( "harry-kerry@uspsrsvp.org" ) ) );
        template.setPublicComments( new LinkedList<>( List.of( "Test POC Template Transform" ) ) );

        PocPayload payload = PocTransformer.createPayload( template );
        Assert.assertEquals( payload.getPocHandle(), template.getPocHandle() );
        Assert.assertEquals( payload.getLastName(), template.getLastName() );
        Assert.assertEquals( payload.getFirstName(), template.getFirstName() );
        Assert.assertEquals( payload.getMiddleName(), template.getMiddleName() );
        Assert.assertEquals( payload.getCompanyName(), template.getCompanyName() );
        Assert.assertEquals( payload.getMultilineStreet().getText(), "923 Orly Yarly\nSuite 202" );
        Assert.assertEquals( payload.getCity(), template.getCity() );
        Assert.assertEquals( payload.getState(), template.getStateProvince() );
        Assert.assertEquals( payload.getPostalCode(), template.getPostalCode() );
        Assert.assertEquals( payload.getCountry().getAlpha2Code(), template.getCountryCode() );
        Assert.assertNotNull( payload.getPhones() );
        Assert.assertEquals( payload.getPhones().size(), 1 );
        Assert.assertEquals( payload.getPhones().get( 0 ).getNumber(), template.getOfficePhone().get( 0 ) );
        Assert.assertEquals( payload.getEmail(), template.getEmail() );
        Assert.assertEquals( payload.getPublicComments(), StringUtils.join( template.getPublicComments(), "\n" ) );
    }

    @Test
    public void testCreatePoc() throws Exception
    {
        // Create the template.
        PocTemplateImpl template = new PocTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( PocTemplateImpl.Action.CREATE );
        template.setLastName( "Kerry" );
        template.setFirstName( "Harry" );
        template.setMiddleName( "Middle" );
        template.setCompanyName( "USPSRSVP" );
        template.getAddress().add( "923 Orly Yarly" );
        template.getAddress().add( "Suite 202" );
        template.setCity( "Anaheim" );
        template.setStateProvince( "CA" );
        template.setPostalCode( "92801" );
        template.setCountryCode( "US" );
        template.setOfficePhone( new LinkedList<>( List.of( "+1-714-523-8888" ) ) );
        template.setOfficePhoneExt( new LinkedList<>( List.of( "" ) ) );
        template.setEmail( new LinkedList<>( List.of( "harry-kerry@uspsrsvp.org" ) ) );

        // Create the payload that will be returned.
        final PocPayload payload = PocTransformer.createPayload( template );
        payload.setPocHandle( "HK31-ARIN" );

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
        Assert.assertTrue( content.contains( "http://whois.arin.net/rest/poc/HK31-ARIN" ) );
    }

    @Test
    public void testCreatePocV5Template() throws Exception
    {
        // Create the template.
        PocTemplateV5Impl template = new PocTemplateV5Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( PocTemplateImpl.Action.CREATE );
        template.setLastName( "Kerry" );
        template.setFirstName( "Harry" );
        template.setMiddleName( "Middle" );
        template.setCompanyName( "USPSRSVP" );
        template.getAddress().add( "923 Orly Yarly" );
        template.getAddress().add( "Suite 202" );
        template.setCity( "Anaheim" );
        template.setStateProvince( "CA" );
        template.setPostalCode( "92801" );
        template.setCountryCode( "US" );
        template.setOfficePhone( new LinkedList<>( List.of( "+1-714-523-8888" ) ) );
        template.setOfficePhoneExt( new LinkedList<>( List.of( "" ) ) );
        template.setEmail( new LinkedList<>( List.of( "harry-kerry@uspsrsvp.org" ) ) );
        template.setPublicComments( new LinkedList<>( List.of( "Test POC Template Transform" ) ) );

        // Create the payload that will be returned.
        final PocPayload payload = PocTransformer.createPayload( template );
        payload.setPocHandle( "HK31-ARIN" );

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
        Assert.assertTrue( content.contains( "http://whois.arin.net/rest/poc/HK31-ARIN" ) );
    }

    @Test
    public void testInvalidPocV5Template() throws Exception
    {
        // Create an invalid template with missing action.
        PocTemplateV5Impl template = new PocTemplateV5Impl();
        template.setPocContactType( "PERSON" );
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setLastName( "Kerry" );
        template.setFirstName( "Harry" );
        template.setMiddleName( "Middle" );
        template.setCompanyName( "USPSRSVP" );
        template.getAddress().add( "923 Orly Yarly" );
        template.getAddress().add( "Suite 202" );
        template.setCity( "Anaheim" );
        template.setStateProvince( "CA" );
        template.setPostalCode( "92801" );
        template.setCountryCode( "US" );
        template.setOfficePhone( new LinkedList<>( List.of( "+1-714-523-8888" ) ) );
        template.setOfficePhoneExt( new LinkedList<>( List.of( "" ) ) );
        template.setEmail( new LinkedList<>( List.of( "harry-kerry@uspsrsvp.org" ) ) );
        template.setPublicComments( new LinkedList<>( List.of( "Test POC Template Transform" ) ) );

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTemplate( template );

        route( templateMessage );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "ARIN was unable to process your request" ) );
        Assert.assertTrue( content.contains( "Invalid registration action" ) );
    }

    @Test
    public void testCreateFailurePoc() throws Exception
    {
        // Create an invalid template with missing last name.
        PocTemplateImpl template = new PocTemplateV5Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( PocTemplateImpl.Action.CREATE );
        template.setFirstName( "Harry" );
        template.setMiddleName( "Middle" );
        template.setCompanyName( "USPSRSVP" );
        template.getAddress().add( "923 Orly Yarly" );
        template.getAddress().add( "Suite 202" );
        template.setCity( "Anaheim" );
        template.setStateProvince( "CA" );
        template.setPostalCode( "92801" );
        template.setCountryCode( "US" );
        template.setOfficePhone( new LinkedList<>( List.of( "+1-714-523-8888" ) ) );
        template.setOfficePhoneExt( new LinkedList<>( List.of( "" ) ) );
        template.setEmail( new LinkedList<>( List.of( "harry-kerry@uspsrsvp.org" ) ) );
        template.setPublicComments( new LinkedList<>( List.of( "Test POC Template Transform" ) ) );

        // Create the payload that will be returned.
        final ErrorPayload payload = new ErrorPayload();
        payload.setCode( ErrorPayload.Code.E_ENTITY_VALIDATION );
        payload.setMessage( "Payload entity failed to validate; see component messages for details." );
        payload.addComponentError( "lastName", "Value is required." );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                createResponse( HttpStatus.SC_BAD_REQUEST, payload );
            }
        } );

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTemplate( template );

        route( templateMessage );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "ARIN was unable to process your request" ) );
        // Mocked error response does not translate to detailed error message.
    }

    @Test
    public void testModifyPoc() throws Exception
    {
        // Create the template with a middle name and a new company name.
        PocTemplateImpl template = new PocTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( PocTemplateImpl.Action.MODIFY );
        template.setPocHandle( "HK31-ARIN" );
        template.setLastName( "Kerry" );
        template.setFirstName( "Harry" );
        template.setMiddleName( "New Middle" );
        template.setMiddleName( "Middle" );
        template.setCompanyName( "USPSRSVP" );
        template.getAddress().add( "923 Orly Yarly" );
        template.getAddress().add( "Suite 202" );
        template.setCity( "Anaheim" );
        template.setStateProvince( "CA" );
        template.setPostalCode( "92801" );
        template.setCountryCode( "US" );
        template.setOfficePhone( new LinkedList<>( List.of( "+1-714-523-8888" ) ) );
        template.setOfficePhoneExt( new LinkedList<>( List.of( "" ) ) );
        template.setEmail( new LinkedList<>( List.of( "harry-kerry@uspsrsvp.org" ) ) );
        template.setPublicComments( new LinkedList<>( List.of( "Test POC Template Transform" ) ) );

        // Create the payload that will be returned.
        final PocPayload getPayload = PocTransformer.createPayload( template );
        final PocPayload putPayload = PocTransformer.modifyPayload( template, new PocPayload() );

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
        Assert.assertTrue( content.contains( "http://whois.arin.net/rest/poc/HK31-ARIN" ) );
    }

    @Test
    public void testModifyPocWithLowercaseStateName()
    {
        PocTemplateImpl template = new PocTemplateV4Impl();
        template.setStateProvince( "va" );

        PocPayload pocPayload = PocTransformer.modifyPayload( template, new PocPayload() );

        Assert.assertEquals( "VA", pocPayload.getState() );
    }

    /**
     * This test asserts that the email addresses that are submitted on the template are replaced on the payload and not
     * added to the payload.
     */
    @Test
    public void testModifyPocEmailsReplace() throws Exception
    {
        // Create the template with a middle name and a new company name.
        PocTemplateImpl template = new PocTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( PocTemplateImpl.Action.MODIFY );
        template.setPocHandle( "HK31-ARIN" );
        template.setLastName( "Kerry" );
        template.setFirstName( "Harry" );
        template.setMiddleName( "New Middle" );
        template.setCompanyName( "USPSRSVP" );
        template.getAddress().add( "923 Orly Yarly" );
        template.getAddress().add( "Suite 202" );
        template.setCity( "Anaheim" );
        template.setStateProvince( "CA" );
        template.setPostalCode( "92801" );
        template.setCountryCode( "US" );
        template.setOfficePhone( new LinkedList<>( List.of( "+1-714-523-8888" ) ) );
        template.setOfficePhoneExt( new LinkedList<>( List.of( "" ) ) );
        template.setEmail( new LinkedList<>( Arrays.asList( "harry-kerry@uspsrsvp.org", "harry-kerry-2@uspsrsvp.org" ) ) );
        template.setPublicComments( new LinkedList<>( List.of( "Test POC Template Transform" ) ) );

        // Create the payload that will be returned.
        final PocPayload getPayload = PocTransformer.createPayload( template );

        PocPayload currentPayload = new PocPayload();
        currentPayload.setEmail( new LinkedList<>( List.of( "email@email.com" ) ) );

        final PocPayload putPayload = PocTransformer.modifyPayload( template, currentPayload );

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

        Assert.assertEquals( putPayload.getEmail().size(), 2 );
        Assert.assertTrue( putPayload.getEmail().contains( "harry-kerry@uspsrsvp.org" ) );
        Assert.assertTrue( putPayload.getEmail().contains( "harry-kerry-2@uspsrsvp.org" ) );
        Assert.assertFalse( putPayload.getEmail().contains( "email@email.com" ) );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "ARIN has completed the processing of your template as requested" ) );
        Assert.assertTrue( content.contains( "http://whois.arin.net/rest/poc/HK31-ARIN" ) );
    }

    /**
     * This tests to ensure that the NONE in the template is parsed appropriately.
     */
    @Test
    public void testModifyPocNone()
    {
        // Create a normal modify template.
        PocTemplateImpl template = new PocTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( PocTemplateImpl.Action.MODIFY );
        template.setPocHandle( "HK31-ARIN" );
        template.setLastName( "Kerry" );
        template.setFirstName( "Harry" );
        template.setMiddleName( "New Middle" );
        template.setCompanyName( "USPSRSVP" );
        template.getAddress().add( "923 Orly Yarly" );
        template.getAddress().add( "Suite 202" );
        template.setCity( "Anaheim" );
        template.setStateProvince( "CA" );
        template.setPostalCode( "92801" );
        template.setCountryCode( "US" );
        template.setOfficePhone( new LinkedList<>( List.of( "+1-714-523-8888" ) ) );
        template.setOfficePhoneExt( new LinkedList<>( List.of( "" ) ) );
        template.setMobilePhone( new LinkedList<>( List.of( "+1-714-427-7878" ) ) );
        template.setEmail( new LinkedList<>( List.of( "harry-kerry@uspsrsvp.org" ) ) );
        template.setPublicComments( new LinkedList<>( List.of( "Test POC Template Transform" ) ) );

        // Create a modify template with NONE values.
        PocTemplateImpl templateNone = new PocTemplateV4Impl();
        templateNone.setApiKey( "API-0000-0000-0000-0000" );
        templateNone.setAction( PocTemplateImpl.Action.MODIFY );
        templateNone.setPocHandle( "HK31-ARIN" );
        templateNone.setCompanyName( "NONE " );
        templateNone.setMiddleName( "NONE" );
        templateNone.setPostalCode( "NONE" );
        templateNone.getAddress().add( "NONE" );
        templateNone.getAddress().add( "NONE" );
        templateNone.setMobilePhone( new LinkedList<>( List.of( "NONE" ) ) );
        templateNone.setPublicComments( new LinkedList<>( List.of( "This is a new public comment!!!" ) ) );

        // Create the payload that will be returned.
        PocPayload putPayload = PocTransformer.modifyPayload( templateNone, PocTransformer.createPayload( template ) );

        Assert.assertEquals( putPayload.getPhones().size(), 1 );
        Assert.assertEquals( putPayload.getPhones().get( 0 ).getType().getCode(), PhoneTypePayload.Code.O );
        Assert.assertNull( putPayload.getMiddleName() );
        Assert.assertNull( putPayload.getPostalCode() );
        Assert.assertNull( putPayload.getCompanyName() );
        Assert.assertEquals( putPayload.getPublicComments(), "This is a new public comment!!!" );
    }

    @Test
    public void testRemovePoc() throws Exception
    {
        // Create the template.
        PocTemplateImpl template = new PocTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( PocTemplateImpl.Action.REMOVE );
        template.setPocHandle( "ABCDE-ARIN" );

        // Create the payload that will be returned.
        final PocPayload payload = PocTransformer.createPayload( template );

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
        Assert.assertTrue( content.contains( "ABCDE-ARIN will be removed shortly." ) );
    }
}
