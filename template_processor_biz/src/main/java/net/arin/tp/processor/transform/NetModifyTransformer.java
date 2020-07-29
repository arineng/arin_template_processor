package net.arin.tp.processor.transform;

import net.arin.tp.ipaddr.IPAddr;
import net.arin.tp.ipaddr.IPRange;
import net.arin.tp.api.payload.NetBlockPayload;
import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.PocLinkPayload;
import net.arin.tp.api.payload.TicketedRequestPayload;
import net.arin.tp.api.service.NetService;
import net.arin.tp.processor.exception.RESTResponseFailure;
import net.arin.tp.processor.exception.TemplateException;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.template.NetModifyTemplateImpl;
import net.arin.tp.processor.utils.MessageBundle;
import net.arin.tp.processor.utils.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import java.util.List;

/**
 * Process the net modification or removal. Net returns of a DA or DS net type should be directly sent to ARIN and not
 * processed via Template Processor/Reg-RWS code. For modifications, DA, DS, A, and S net types may be processed.
 * <br/>
 * Any attempt to modify or remove a simple reassign network results in an error message. The user must submit a
 * REASSIGN-SIMPLE template to process simple reassigned networks.
 */
public class NetModifyTransformer extends NetTransformer
{
    private static final Logger log = LoggerFactory.getLogger( NetModifyTransformer.class );
    private NetService netService;

    @Override
    public Message transform( TemplateMessage message )
            throws TemplateException
    {
        log.debug( "Transforming template: " + message.getTemplate().toString() );

        initNetService();

        Message response;
        NetModifyTemplateImpl template = ( NetModifyTemplateImpl ) message.getTemplate();

        try
        {
            IPRange ipRange = convertIPAddressFieldToIPRange( template.getIpAddress(), template.getIPVersion() );

            // Using the IP address supplied by the template, we'll load the network to be modified.
            NetPayload netPayload = getNetwork( ipRange.getStartIPAddress(), ipRange.getEndIPAddress(), template.getApiKey() );

            confirmAppropriateNetType( netPayload );

            if ( template.getAction() == null )
            {
                throw new TemplateException( MessageBundle.UNKNOWN_TEMPLATE_ACTION_MESSAGE );
            }

            switch ( template.getAction() )
            {
                case MODIFY:
                    response = modifyNet( message, template, netPayload );
                    break;
                case REMOVE:
                    response = removeNet( message, template, netPayload );
                    break;
                default:
                    throw new TemplateException( MessageBundle.UNKNOWN_TEMPLATE_ACTION_MESSAGE );
            }
        }
        catch ( ClientResponseFailure crp )
        {
            throw new RESTResponseFailure( crp );
        }

        return response;
    }

    /**
     * For either a MODIFY or REMOVE action, the NET-MOD template may not act on a simple reassign. The user should send
     * in a SIMPLE-REASSIGN template instead.
     *
     * @param payload net payload being acted upon
     */
    private void confirmAppropriateNetType( NetPayload payload )
    {
        // If the net payload represents a simple reassign, throw an error.
        if ( ( NetBlockPayload.NetType.REASSIGNED.equals( payload.getNetType() ) )
                && StringUtils.isNotEmpty( payload.getCustomerHandle() ) )
        {
            throw new TemplateException( MessageBundle.NET_MODIFY_SIMPLE_REASSIGN );
        }
    }

    private Message modifyNet( TemplateMessage message, NetModifyTemplateImpl template, NetPayload netPayload )
    {
        log.debug( "Net MODIFY action being processed..." );

        modifyPayload( template, netPayload );

        NetPayload result = this.netService.modify( netPayload.getNetHandle(), template.getApiKey(), netPayload );
        log.debug( "Modified net: " + result.getNetHandle() );

        return Response.netSuccessful( message, result );
    }

    private Message removeNet( TemplateMessage message, NetModifyTemplateImpl template, NetPayload netPayload )
    {
        log.debug( "Net REMOVE action being processed..." );

        Message response;

        if ( !requiresReview( netPayload ) )
        {
            setAttachmentsAndAdditionalInfo( netPayload, template );

            TicketedRequestPayload ticketedResponse = this.netService.remove( netPayload.getNetHandle(), template.getApiKey(), netPayload );
            log.debug( "Removed (or ticketed the removal) of net: " + netPayload.getNetHandle() );

            if ( ticketedResponse.getTicket() != null )
            {
                response = Response.netDeleteTicketed( message, ticketedResponse.getTicket(), netPayload );
            }
            else
            {
                response = Response.netSuccessful( message, ticketedResponse.getNet() );
            }
        }
        else
        {
            throw new TemplateException( MessageBundle.NET_MOD_TEMPLATE_INVALID_REMOVE );
        }

        return response;
    }

    private NetPayload getNetwork( IPAddr start, IPAddr end, String apiKey )
    {
        List<NetPayload> results = netService.netsByIpRange( start.toShortNotation(), end.toShortNotation(), apiKey ).getPayloads();

        // The previous method call with throw an exception if NO networks are found for this range. So our logic below
        // is confined to:
        if ( results.size() > 1 )
        {
            throw new TemplateException( MessageBundle.NET_MOD_TEMPLATE_MULTIPLE_NETS_FOUND );
        }

        return results.get( 0 );
    }

    private boolean requiresReview( NetPayload payload )
    {
        return NetBlockPayload.NetType.DIRECT_ALLOCATION.equals( payload.getNetType() )
                || NetBlockPayload.NetType.DIRECT_ASSIGNMENT.equals( payload.getNetType() );
    }

    protected static void modifyPayload( NetModifyTemplateImpl template, NetPayload payload )
    {
        if ( isNone( template.getNetName() ) )
        {
            payload.setNetName( null );
        }
        else if ( !StringUtils.isEmpty( template.getNetName() ) )
        {
            payload.setNetName( template.getNetName() );
        }

        TransformerAssistant.transformOriginAses( template, payload );

        TransformerAssistant.transformPocs( PocLinkPayload.Function.T, payload, template.getTechPocHandles(),
                NetModifyTemplateImpl.TECH_POC_HANDLE_LABEL );
        TransformerAssistant.transformPocs( PocLinkPayload.Function.AB, payload, template.getAbusePocHandles(),
                NetModifyTemplateImpl.ABUSE_POC_HANDLE_LABEL );
        TransformerAssistant.transformPocs( PocLinkPayload.Function.N, payload, template.getNocPocHandles(),
                NetModifyTemplateImpl.NOC_POC_HANDLE_LABEL );

        TransformerAssistant.transformPublicComments( template, payload );
    }

    private void initNetService()
    {
        if ( this.netService == null )
        {
            this.netService = ServiceLocator.getNetService();
        }
    }
}
