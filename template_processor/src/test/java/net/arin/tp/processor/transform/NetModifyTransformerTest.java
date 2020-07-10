package net.arin.tp.processor.transform;

import net.arin.tp.api.payload.NetBlockPayload;
import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.PayloadList;
import net.arin.tp.api.payload.TicketPayload;
import net.arin.tp.api.payload.TicketedRequestPayload;
import net.arin.tp.api.utils.CollectionUtils;
import net.arin.tp.processor.BaseTest;
import net.arin.tp.processor.exception.TemplateRequiresReviewException;
import net.arin.tp.processor.message.MailMessage;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.template.IPV4NetModifyTemplateV5Impl;
import net.arin.tp.processor.template.NetModifyTemplateImpl;
import net.arin.tp.processor.template.Template;
import net.arin.tp.processor.template.TemplateImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.plugins.providers.jaxb.JAXBUnmarshalException;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jms.Message;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NetModifyTransformerTest extends BaseTest
{
    private static final String WRONG_TEMPLATE_MESSAGE = "The network can not be modified or removed with the NET-MOD template. Please use the REASSIGN-SIMPLE template for that purpose.";

    @Test
    public void testModifySuccess() throws Exception
    {
        // Create the template.
        NetModifyTemplateImpl template = new IPV4NetModifyTemplateV5Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( NetModifyTemplateImpl.Action.MODIFY );
        template.setIpAddress( "112.164.200.000/24" );

        // Set up a net payload.
        final NetPayload netPayload = getNetPayloadWithoutCustomerOrOrg( "NET-HANDLE",
                NetBlockPayload.NetType.REALLOCATED, "112.164.200.000", "112.164.200.255" );
        netPayload.setOrgHandle( "SAMPLE-ORG" );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                // Get net associated to the IP address.
                createPayloadListResponse( HttpStatus.SC_OK, getPayloadListWrapper( netPayload ) );

                // Modify the net.
                createResponse( HttpStatus.SC_OK, netPayload );
            }
        } );

        Message msg = routeTemplateMessage( template );

        Assert.assertNull( msg );

        String content = assertPresenceOfMessageInQueueAndReturnContent();
        Assert.assertTrue( content.contains( "ARIN has completed the processing of your template as requested" ) );
        Assert.assertTrue( content.contains( "The Net Handle for your network record is NET-HANDLE." ) );
    }

    @Test
    public void testRemoveDirectAllocation() throws Exception
    {
        // Create the template.
        NetModifyTemplateImpl template = new IPV4NetModifyTemplateV5Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( NetModifyTemplateImpl.Action.REMOVE );
        template.setIpAddress( "112.164.200.000/24" );

        // Set up a net payload.
        final NetPayload netPayload = getNetPayloadWithoutCustomerOrOrg( "NET-HANDLE",
                NetBlockPayload.NetType.DIRECT_ALLOCATION, "112.164.200.000", "112.164.200.255" );
        netPayload.setOrgHandle( "SAMPLE-ORG" );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                // Get net associated to the IP address.
                createPayloadListResponse( HttpStatus.SC_OK, getPayloadListWrapper( netPayload ) );
            }
        } );

        try
        {
            routeTemplateMessage( template );
        }
        catch ( RuntimeException re )
        {
            Throwable throwable = re.getCause();
            if ( !( throwable instanceof JAXBUnmarshalException ) )
            {
                Assert.assertEquals( re.getCause().getClass(), TemplateRequiresReviewException.class );
            }
        }
    }

    @Test
    public void testModifyFailsForSimpleReassign() throws Exception
    {
        testSimpleReassignActionAttempt( TemplateImpl.Action.MODIFY );
    }

    @Test
    public void testRemoveSingleNetSuccess() throws Exception
    {
        String content = testRemoveNet( false );
        Assert.assertTrue( content.contains( "ARIN has completed the processing of your template as requested" ) );
        Assert.assertTrue( content.contains( "NET-HANDLE will be removed shortly." ) );
    }

    @Test
    public void testRemoveNetTicketedSuccess() throws Exception
    {
        String content = testRemoveNet( true );
        Assert.assertTrue( content.contains( "Your request to remove the network record for NET-HANDLE has been assigned ticket number X-1001" ) );
    }

    @Test
    public void testRemoveFailsForSimpleReassign() throws Exception
    {
        testSimpleReassignActionAttempt( TemplateImpl.Action.REMOVE );
    }

    @Test
    public void testTemplateToPayloadTransfer()
    {
        final String NONE = "NONE";

        // Create a template with values (no NONEs).
        NetModifyTemplateImpl template = new IPV4NetModifyTemplateV5Impl();
        template.setNetName( "Test net name" );
        template.setOriginAses( Arrays.asList( "AS123", "456" ) );
        template.setPublicComments( Arrays.asList( "comment 1-2-3" ) );

        template.getTechPocHandles().addAll( Arrays.asList( "TECH-POC1", "TECH-POC2" ) );
        template.getAbusePocHandles().add( "ABUSE-POC1" );
        template.getNocPocHandles().addAll( Arrays.asList( "NOC-POC1", "NOC-POC2", "NOC-POC3" ) );

        NetPayload netPayload = new NetPayload();
        NetModifyTransformerExt.modifyPayload( template, netPayload );

        Assert.assertEquals( template.getNetName(), netPayload.getNetName() );
        Assert.assertTrue( CollectionUtils.equalContents( netPayload.getTechPocs(), template.getTechPocHandles() ) );
        Assert.assertTrue( CollectionUtils.equalContents( netPayload.getAbusePocs(), template.getAbusePocHandles() ) );
        Assert.assertTrue( CollectionUtils.equalContents( netPayload.getNocPocs(), template.getNocPocHandles() ) );
        Assert.assertTrue( CollectionUtils.equalContents( netPayload.getOriginAses(), template.getOriginAses() ) );
        Assert.assertEquals( netPayload.getPublicComments(), "comment 1-2-3" );
        Assert.assertTrue( CollectionUtils.equalContents( netPayload.getNocPocs(), template.getNocPocHandles() ) );

        // Check that values that are NONE in the template are properly cleared by the modifyPayload method.
        template.setNetName( NONE );
        template.getTechPocHandles().clear();
        template.getTechPocHandles().add( NONE );
        template.getAbusePocHandles().clear();
        template.getAbusePocHandles().add( NONE );
        template.getNocPocHandles().clear();
        template.getNocPocHandles().add( NONE );

        // if any are NONE, they should get wiped. Yeah, this is inconsistent with how POCs are handled.
        template.getOriginAses().set( 1, NONE );

        NetModifyTransformerExt.modifyPayload( template, netPayload );

        // Now the appropriate values should have been cleared in the payload based on NONE in the template.
        Assert.assertTrue( StringUtils.isEmpty( netPayload.getNetName() ) );
        Assert.assertTrue( netPayload.getTechPocs().isEmpty() );
        Assert.assertTrue( netPayload.getAbusePocs().isEmpty() );
        Assert.assertTrue( netPayload.getNocPocs().isEmpty() );
        Assert.assertTrue( netPayload.getOriginAses().isEmpty() );
    }

    private void testSimpleReassignActionAttempt( TemplateImpl.Action action ) throws Exception
    {
        // Create the template.
        NetModifyTemplateImpl template = new IPV4NetModifyTemplateV5Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( action );
        template.setIpAddress( "112.164.200.000/24" );

        // Set up a net payload that looks like a simple reassignment.
        final NetPayload netPayload = getSimpleReassignedPayload( "NET-HANDLE", "112.164.200.000", "112.164.200.255" );

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                // Get simple reassigned net associated to the IP address.
                createPayloadListResponse( HttpStatus.SC_OK, getPayloadListWrapper( netPayload ) );
            }
        } );

        routeTemplateMessage( template );

        String result = assertPresenceOfMessageInQueueAndReturnContent();
        Assert.assertTrue( result.contains( WRONG_TEMPLATE_MESSAGE ) );
    }

    private String testRemoveNet( boolean isTicketed ) throws Exception
    {
        // Create the template.
        NetModifyTemplateImpl template = new IPV4NetModifyTemplateV5Impl();
        template.setApiKey( "API-0000-0000-0000-0000" );
        template.setAction( NetModifyTemplateImpl.Action.REMOVE );
        template.setIpAddress( "112.164.200.000/24" );

        // Set up a net payload.
        final NetPayload netPayload = getNetPayloadWithoutCustomerOrOrg( "NET-HANDLE",
                NetBlockPayload.NetType.REALLOCATED, "112.164.200.000", "112.164.200.255" );
        netPayload.setOrgHandle( "SAMPLE-ORG" );

        final TicketedRequestPayload ticketedRequestPayload = new TicketedRequestPayload();
        if ( isTicketed )
        {
            TicketPayload ticket = new TicketPayload();
            ticket.setTicketNo( "X-1001" );
            ticketedRequestPayload.setTicket( ticket );
        }
        else
        {
            ticketedRequestPayload.setNet( netPayload );
        }

        this.setupMock( new AbstractSetupAssistant()
        {
            public void coreSetup() throws Exception
            {
                // Get net associated to the IP address.
                createPayloadListResponse( HttpStatus.SC_OK, getPayloadListWrapper( netPayload ) );

                // Net that was removed.
                createResponse( HttpStatus.SC_OK, ticketedRequestPayload );
            }
        } );

        Message msg = routeTemplateMessage( template );
        Assert.assertNull( msg );

        return assertPresenceOfMessageInQueueAndReturnContent();
    }

    /**
     * Used solely to allow us access to the protected method for our testing.
     */
    private class NetModifyTransformerExt extends NetModifyTransformer
    {
    }

    private static NetPayload getSimpleReassignedPayload( String netHandle, String start, String end )
    {
        NetPayload netPayload = getNetPayloadWithoutCustomerOrOrg( netHandle, NetBlockPayload.NetType.REASSIGNED, start, end );
        netPayload.setCustomerHandle( "CUST-ORG" );
        return netPayload;
    }

    private static NetPayload getNetPayloadWithoutCustomerOrOrg( String netHandle, NetBlockPayload.NetType netType, String start, String end )
    {
        NetPayload netPayload = new NetPayload();
        NetBlockPayload netBlockPayload = new NetBlockPayload();
        netBlockPayload.setType( netType );
        netBlockPayload.setStartAddress( start );
        netBlockPayload.setEndAddress( end );
        netPayload.getNetBlocks().add( netBlockPayload );
        netPayload.setNetHandle( netHandle );
        return netPayload;
    }

    private static PayloadList<NetPayload> getPayloadListWrapper( NetPayload... payloads )
    {
        PayloadList<NetPayload> pl = new PayloadList<>();
        for ( NetPayload p : payloads )
        {
            pl.getPayloads().add( p );
        }
        return pl;
    }

    private Message routeTemplateMessage( Template template )
    {
        MailMessage mm = new MailMessage( getMimeMessage( "wuser@localhost.com", "to@localhost.com",
                "cc@localhost.com", "subject", "body", new ArrayList<>(), new HashMap<>() ) );
        TemplateMessage templateMessage = new TemplateMessage( mm );
        templateMessage.setTemplate( template );
        templateMessage.setFrom( new ArrayList<>( Arrays.asList( "wuser@localhost.com" ) ) );
        templateMessage.setReplyTo( new ArrayList<>( Arrays.asList( "sample@sample.com" ) ) );
        return route( templateMessage );
    }
}
