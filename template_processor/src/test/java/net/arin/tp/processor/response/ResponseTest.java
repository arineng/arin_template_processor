package net.arin.tp.processor.response;

import net.arin.tp.api.payload.CustomerPayload;
import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.OrgPayload;
import net.arin.tp.api.payload.PocPayload;
import net.arin.tp.api.payload.TicketPayload;
import net.arin.tp.processor.BaseTest;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.template.OrgTemplateImpl;
import net.arin.tp.processor.template.PocTemplateTest;
import net.arin.tp.processor.template.TemplateImpl;
import net.arin.tp.processor.transform.OrgTransformer;
import net.arin.tp.processor.util.MockTemplates;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;

public class ResponseTest extends BaseTest
{
    private static final String HOSTMASTER_ADDRESS = "hostmaster@bogus.example";

    @Test
    public void testPocResponse() throws Exception
    {
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setSubject( "POC_TEMPLATE" );
        templateMessage.setTo( new ArrayList<>()
        {{
            add( HOSTMASTER_ADDRESS );
        }} );
        templateMessage.setFrom( new ArrayList<>()
        {{
            add( "josh@occupation.net" );
        }} );
        templateMessage.setCc( new ArrayList<>()
        {{
            add( "carboncopy@occupation.net" );
        }} );

        Response.setQueueOnly( true );
        Message message = Response.generalFailure( templateMessage, "This is just a test" );
        Response.send( message );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        Assert.assertEquals( response.getSubject(), "Re: POC_TEMPLATE --" + ResponseType.REJECTED );
        Assert.assertEquals( getToAddresses( response ).size(), 1 );
        Assert.assertTrue( getToAddresses( response ).contains( "josh@occupation.net" ) );
        Assert.assertEquals( getCcAddresses( response ).size(), 1 );
        Assert.assertTrue( getCcAddresses( response ).contains( "carboncopy@occupation.net" ) );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "ARIN was unable to process your request" ) );
        Assert.assertTrue( content.contains( "This is just a test" ) );
    }

    @Test
    public void testResponseSubject() throws Exception
    {
        String subject = "Detailed Reassign Successful";

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setSubject( subject );

        NetPayload netPayload = new NetPayload();
        OrgPayload orgPayload = new OrgPayload();
        PocPayload pocPayload = new PocPayload();
        CustomerPayload customerPayload = new CustomerPayload();
        TicketPayload ticketPayload = new TicketPayload();

        Response.setQueueOnly( true );
        Message message = Response.detailedReassignSuccessful( templateMessage, netPayload, orgPayload );
        Response.send( message );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().remove( 0 );
        Assert.assertEquals( response.getSubject(), "Re: " + subject + " --" + ResponseType.APPROVED );

        subject = "General Failure";
        templateMessage.setSubject( subject );
        message = Response.generalFailure( templateMessage, "General Errors" );
        Response.send( message );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        response = Response.getMessageQueue().remove( 0 );
        Assert.assertEquals( response.getSubject(), "Re: " + subject + " --" + ResponseType.REJECTED );

        subject = "Net Successful";
        templateMessage.setSubject( subject );
        message = Response.netSuccessful( templateMessage, netPayload );
        Response.send( message );
        log.info( "{}", message.getContent() );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        response = Response.getMessageQueue().remove( 0 );
        Assert.assertEquals( response.getSubject(), "Re: " + subject + " --" + ResponseType.APPROVED );

        subject = "Org Successful";
        templateMessage.setSubject( subject );
        templateMessage.setTemplate( TemplateImpl.createTemplate( MockTemplates.getV5OrgTemplate() ) );
        orgPayload = OrgTransformer.createPayload( ( OrgTemplateImpl ) templateMessage.getTemplate() );
        log.info( "{}", orgPayload );
        message = Response.orgSuccessful( templateMessage, orgPayload );
        Response.send( message );
        log.info( "{}", message.getContent() );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        response = Response.getMessageQueue().remove( 0 );
        Assert.assertEquals( response.getSubject(), "Re: " + subject + " --" + ResponseType.APPROVED );

        subject = "Poc Successful";
        templateMessage.setSubject( subject );
        templateMessage.setText( PocTemplateTest.getPocTemplate() );
        message = Response.pocSuccessful( templateMessage, pocPayload );
        Response.send( message );
        log.info( "{}", message.getContent() );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        response = Response.getMessageQueue().remove( 0 );
        Assert.assertEquals( response.getSubject(), "Re: " + subject + " --" + ResponseType.APPROVED );

        subject = "Reallocate Successful";
        templateMessage.setSubject( subject );
        message = Response.reallocateSuccessful( templateMessage, netPayload, orgPayload );
        Response.send( message );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        response = Response.getMessageQueue().remove( 0 );
        Assert.assertEquals( response.getSubject(), "Re: " + subject + " --" + ResponseType.APPROVED );

        subject = "Simple Reassign Successful";
        templateMessage.setSubject( subject );
        message = Response.simpleReassignSuccessful( templateMessage, netPayload, customerPayload );
        Response.send( message );
        log.info( "{}", message.getContent() );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        response = Response.getMessageQueue().remove( 0 );
        Assert.assertEquals( response.getSubject(), "Re: " + subject + " --" + ResponseType.APPROVED );

        subject = "Ticketed Net Delete";
        templateMessage.setSubject( subject );
        message = Response.netDeleteTicketed( templateMessage, ticketPayload, netPayload );
        Response.send( message );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        response = Response.getMessageQueue().remove( 0 );
        Assert.assertEquals( response.getSubject(), "Re: " + subject );

        subject = "Ticketed Org";
        templateMessage.setSubject( subject );
        message = Response.createOrgSuccessful( templateMessage, ticketPayload );
        Response.send( message );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        response = Response.getMessageQueue().remove( 0 );
        Assert.assertEquals( response.getSubject(), "Re: " + subject );

        subject = "Ticketed Reallocate";
        templateMessage.setSubject( subject );
        message = Response.reallocateTicketed( templateMessage, ticketPayload, orgPayload );
        Response.send( message );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        response = Response.getMessageQueue().remove( 0 );
        Assert.assertEquals( response.getSubject(), "Re: " + subject );

        subject = "Ticketed Simple Reassign";
        templateMessage.setSubject( subject );
        message = Response.simpleReassignTicketed( templateMessage, ticketPayload, customerPayload );
        Response.send( message );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        response = Response.getMessageQueue().remove( 0 );
        Assert.assertEquals( response.getSubject(), "Re: " + subject );
    }

    private List<String> getToAddresses( Message mailMessage ) throws MessagingException
    {
        List<String> retval = new ArrayList<>();
        for ( Address address : mailMessage.getRecipients( Message.RecipientType.TO ) )
        {
            retval.add( ( ( InternetAddress ) address ).getAddress() );
        }
        return retval;
    }

    private List<String> getCcAddresses( Message mailMessage ) throws MessagingException
    {
        List<String> retval = new ArrayList<>();
        for ( Address address : mailMessage.getRecipients( Message.RecipientType.CC ) )
        {
            retval.add( ( ( InternetAddress ) address ).getAddress() );
        }
        return retval;
    }
}
