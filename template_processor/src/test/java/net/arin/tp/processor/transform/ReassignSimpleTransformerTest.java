package net.arin.tp.processor.transform;

import net.arin.tp.api.payload.CustomerPayload;
import net.arin.tp.api.payload.NetBlockPayload;
import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.TicketedRequestPayload;
import net.arin.tp.processor.BaseTest;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.template.ReassignSimpleTemplateImpl;
import net.arin.tp.processor.template.ReassignSimpleTemplateV4Impl;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.mail.Message;
import java.util.ArrayList;
import java.util.List;

public class ReassignSimpleTransformerTest extends BaseTest
{
    @Test
    public void testRemoveReassignSimple() throws Exception
    {
        // Create the template.
        ReassignSimpleTemplateImpl template = new ReassignSimpleTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( ReassignSimpleTemplateImpl.Action.REMOVE );
        template.setIpAddress( "112.164.200.000/24" );

        CustomerPayload customerPayload = new CustomerPayload();
        final NetPayload netPayload = new NetPayload();
        ReassignSimpleTransformer.createPayload( template, netPayload, customerPayload );
        netPayload.setNetHandle( "NET-HANDLE" );

        final TicketedRequestPayload ticketedRequestPayload = new TicketedRequestPayload();
        ticketedRequestPayload.setNet( netPayload );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                // Get net associated to the IP address.
                createResponse( HttpStatus.SC_OK, netPayload );

                // Delete net.
                createResponse( HttpStatus.SC_OK, ticketedRequestPayload );
            }
        } );

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTemplate( template );

        route( templateMessage );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "ARIN has completed the processing of your template as requested" ) );
        Assert.assertTrue( content.contains( "NET-HANDLE will be removed shortly.  PLEASE NOTE: the associated customer record has not been removed.  If you want to delete the customer record associated with the network, you will need to do so via a RESTful call." ) );
    }

    @Test
    public void testCreateReassignSimple() throws Exception
    {
        // Create the template.
        ReassignSimpleTemplateImpl template = new ReassignSimpleTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( ReassignSimpleTemplateImpl.Action.CREATE );
        template.setIpAddress( "112.164.200.000/24" );
        template.setPrivateCustomer( "Yes" );
        template.setCustomerName( "Name" );
        template.getAddress().add( "Address" );
        template.setCity( "City" );
        template.setStateProvince( "VA" );
        template.setPostalCode( "20147" );
        template.setCountryCode( "US" );

        final CustomerPayload customerPayload = new CustomerPayload();
        final NetPayload netPayload = new NetPayload();
        ReassignSimpleTransformer.createPayload( template, netPayload, customerPayload );
        netPayload.setNetHandle( "NET-HANDLE" );
        netPayload.setCustomerHandle( "CUST-HANDLE" );

        final TicketedRequestPayload ticketedRequestPayload = new TicketedRequestPayload();
        ticketedRequestPayload.setNet( netPayload );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                // Get parent net details.
                createResponse( HttpStatus.SC_OK, netPayload );

                // Create simple reassign.
                createResponse( HttpStatus.SC_OK, ticketedRequestPayload );
            }
        } );

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTemplate( template );

        route( templateMessage );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "ARIN has completed the processing of your template as requested" ) );
        Assert.assertTrue( content.contains( "The Customer Handle for your customer is CUST-HANDLE." ) );
        Assert.assertTrue( content.contains( "The Net Handle for the reassigned network is NET-HANDLE." ) );
    }

    @Test
    public void testCreateReassignSimpleFailsWithTooBigCidr() throws Exception
    {
        // Create the template.
        ReassignSimpleTemplateImpl template = new ReassignSimpleTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( ReassignSimpleTemplateImpl.Action.CREATE );
        template.setIpAddress( "192.110.208.0/98" );

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTemplate( template );

        route( templateMessage );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "ARIN was unable to process your request:" ) );
        Assert.assertTrue( content.contains( "Invalid prefix length used: 98" ) );
    }

    @Test
    public void testModifyReassignSimple() throws Exception
    {
        // Create the template.
        ReassignSimpleTemplateImpl template = new ReassignSimpleTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( ReassignSimpleTemplateImpl.Action.MODIFY );
        template.setIpAddress( "112.164.200.000/24" );

        final CustomerPayload customerPayload = new CustomerPayload();
        final NetPayload netPayload = new NetPayload();
        ReassignSimpleTransformer.createPayload( template, netPayload, customerPayload );
        netPayload.setNetHandle( "NET-HANDLE" );
        netPayload.setParentNetHandle( "PARENT-NET-HANDLE" );
        netPayload.setCustomerHandle( "CUST-HANDLE" );
        customerPayload.setHandle( "CUST-HANDLE" );

        final TicketedRequestPayload ticketedRequestPayload = new TicketedRequestPayload();
        ticketedRequestPayload.setNet( netPayload );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                // Find network.
                createResponse( HttpStatus.SC_OK, netPayload );

                // Get customer details.
                createResponse( HttpStatus.SC_OK, customerPayload );

                // Modify simple reassign.
                createResponse( HttpStatus.SC_OK, netPayload );
            }
        } );

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTemplate( template );

        route( templateMessage );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "ARIN has completed the processing of your template as requested" ) );
        Assert.assertTrue( content.contains( "The Customer Handle for your customer is CUST-HANDLE." ) );
        Assert.assertTrue( content.contains( "The Net Handle for the reassigned network is NET-HANDLE." ) );
    }

    @Test
    public void testModifyReassignSimpleFailsForDetailedReassign() throws Exception
    {
        // Create the template.
        ReassignSimpleTemplateImpl template = new ReassignSimpleTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( ReassignSimpleTemplateImpl.Action.MODIFY );
        template.setIpAddress( "112.164.200.000/24" );

        final NetPayload netPayload = new NetPayload();
        ReassignSimpleTransformer.createPayload( template, netPayload, new CustomerPayload() );
        netPayload.setNetHandle( "NET-HANDLE" );
        netPayload.setParentNetHandle( "PARENT-NET-HANDLE" );

        // Make sure that the net returned is a detailed reassign:
        // a) Net type is REASSIGNED.
        // b) Net is reassigned to an org and not a customer.
        netPayload.setCustomerHandle( null );
        netPayload.setOrgHandle( "ORG-HANDLE" );
        netPayload.setNetTypeOnBlocks( NetBlockPayload.NetType.REASSIGNED );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                // Find network.
                createResponse( HttpStatus.SC_OK, netPayload );
            }
        } );

        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTemplate( template );

        route( templateMessage );

        Assert.assertEquals( Response.getMessageQueue().size(), 1 );
        Message response = Response.getMessageQueue().get( 0 );
        String content = ( String ) response.getContent();
        Assert.assertTrue( content.contains( "The network can not be modified or removed with the REASSIGN-SIMPLE template. Please use the NET-MOD template for that purpose." ) );
    }

    @Test
    public void testModifyOriginAsesWipeToNone()
    {
        ReassignSimpleTemplateImpl template = new ReassignSimpleTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( ReassignSimpleTemplateImpl.Action.MODIFY );
        template.setIpAddress( "112.164.200.000/24" );
        template.getOriginAses().add( "NONE" );

        CustomerPayload customerPayload = new CustomerPayload();
        NetPayload netPayload = new NetPayload();

        netPayload.getOriginAses().add( "AS1001" );
        netPayload.getOriginAses().add( "AS2001" );
        netPayload.getOriginAses().add( "AS3001" );

        ReassignSimpleTransformer.modifyPayload( template, netPayload, customerPayload );

        // The NONE value caused origin AS list to get cleared.
        Assert.assertEquals( netPayload.getOriginAses().size(), 0 );
    }

    @Test
    public void testModifyOriginAsesLeftAlone()
    {
        ReassignSimpleTemplateImpl template = new ReassignSimpleTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( ReassignSimpleTemplateImpl.Action.MODIFY );
        template.setIpAddress( "112.164.200.000/24" );

        CustomerPayload customerPayload = new CustomerPayload();
        NetPayload netPayload = new NetPayload();

        netPayload.getOriginAses().add( "AS1001" );
        netPayload.getOriginAses().add( "AS2001" );
        netPayload.getOriginAses().add( "AS3001" );

        ReassignSimpleTransformer.modifyPayload( template, netPayload, customerPayload );

        // No value in the template caused origin AS list to be left alone.
        Assert.assertEquals( netPayload.getOriginAses().size(), 3 );

        List<String> toMatch = new ArrayList<>();
        toMatch.add( "AS1001" );
        toMatch.add( "AS2001" );
        toMatch.add( "AS3001" );
        Assert.assertEquals( netPayload.getOriginAses(), toMatch );
    }

    @Test
    public void testModifyOriginAsesReplace()
    {
        ReassignSimpleTemplateImpl template = new ReassignSimpleTemplateV4Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( ReassignSimpleTemplateImpl.Action.MODIFY );
        template.setIpAddress( "112.164.200.000/24" );
        template.getOriginAses().add( "AS987" );

        CustomerPayload customerPayload = new CustomerPayload();
        NetPayload netPayload = new NetPayload();

        netPayload.getOriginAses().add( "AS1001" );
        netPayload.getOriginAses().add( "AS2001" );
        netPayload.getOriginAses().add( "AS3001" );

        ReassignSimpleTransformer.modifyPayload( template, netPayload, customerPayload );

        // One or more values in the template should cause origin AS values to be replaced.
        Assert.assertEquals( netPayload.getOriginAses().size(), 1 );

        List<String> toMatch = new ArrayList<>();
        toMatch.add( "AS987" );
        Assert.assertEquals( netPayload.getOriginAses(), toMatch );
    }
}
