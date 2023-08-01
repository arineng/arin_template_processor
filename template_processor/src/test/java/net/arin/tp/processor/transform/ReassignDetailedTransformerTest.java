package net.arin.tp.processor.transform;

import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.OrgPayload;
import net.arin.tp.api.payload.TicketedRequestPayload;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.template.ComplexSwipTemplateImpl;
import net.arin.tp.processor.template.IPV4ReassignDetailedTemplateV4Impl;
import net.arin.tp.processor.template.ReassignDetailedTemplateImpl;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.mail.Message;

public class ReassignDetailedTransformerTest extends ComplexSwipTransformerTest
{
    @Override
    protected ComplexSwipTemplateImpl getTemplateImpl()
    {
        return new IPV4ReassignDetailedTemplateV4Impl();
    }

    @Test
    public void testBasicReassign() throws Exception
    {
        ReassignDetailedTemplateImpl template = new IPV4ReassignDetailedTemplateV4Impl();

        template.setApiKey( "API-0000-0000-0000-0000" );
        template.getEmbeddedOrg().setOrgHandle( "DREASSIGN-ORG1-HANDLE" );
        template.setIpAddress( "112.164.200.000/24" );
        template.setNetName( "Network Name" );

        // For when we query for the parent network.
        final NetPayload netPayload = new NetPayload();
        netPayload.setNetHandle( "DREASSIGN-PARENTNET1-HANDLE" );

        // For when we query for the organization.
        final OrgPayload orgPayload = new OrgPayload();
        orgPayload.setOrgHandle( "DREASSIGN-ORG1-HANDLE" );

        // For when we actually perform the reassign.
        final TicketedRequestPayload ticketedRequestPayload = new TicketedRequestPayload();
        NetPayload resultingNetPayload = new NetPayload();
        resultingNetPayload.setNetHandle( "DREASSIGN-RESULT1-HANDLE" );
        ticketedRequestPayload.setNet( resultingNetPayload );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                createResponse( HttpStatus.SC_OK, netPayload );
                createResponse( HttpStatus.SC_OK, orgPayload );
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
        Assert.assertTrue( content.contains( "The Organization Handle for your ORG is DREASSIGN-ORG1-HANDLE." ) );
        Assert.assertTrue( content.contains( "The Net Handle for the reassigned network is DREASSIGN-RESULT1-HANDLE." ) );
    }

    @Test
    public void testOrgNotFound() throws Exception
    {
        ReassignDetailedTemplateImpl template = new IPV4ReassignDetailedTemplateV4Impl();

        template.setApiKey( "API-0000-0000-0000-0000" );
        template.getEmbeddedOrg().setOrgHandle( "DREASSIGN-ORG1-HANDLE" );
        template.setIpAddress( "112.164.200.000/24" );
        template.setNetName( "Network Name" );

        final NetPayload netPayload = new NetPayload();
        netPayload.setNetHandle( "DREASSIGN-PARENTNET1-HANDLE" );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                // For when we query for the parent network.
                createResponse( HttpStatus.SC_OK, netPayload );

                // For when we query for the organization.
                createResponse( HttpStatus.SC_NOT_FOUND, getErrorPayload( "A sample error message" ) );
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
}
