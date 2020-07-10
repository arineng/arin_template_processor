package net.arin.tp.processor.transform;

import net.arin.tp.ipaddr.IPRange;
import net.arin.tp.ipaddr.IPVersion;
import net.arin.tp.api.payload.CountryPayload;
import net.arin.tp.api.payload.CustomerPayload;
import net.arin.tp.api.payload.NetBlockPayload;
import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.TicketedRequestPayload;
import net.arin.tp.api.service.CustomerService;
import net.arin.tp.api.service.NetService;
import net.arin.tp.processor.exception.RESTResponseFailure;
import net.arin.tp.processor.exception.TemplateException;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.template.ReassignSimpleTemplateImpl;
import net.arin.tp.processor.utils.MessageBundle;
import net.arin.tp.processor.utils.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import java.util.Collections;

public class ReassignSimpleTransformer extends NetTransformer
{
    private static final String Y = "Y";
    private static final String PRIVATE_CUSTOMER = "PRIVATE CUSTOMER";

    private static final Logger log = LoggerFactory.getLogger( ReassignSimpleTransformer.class );

    @Override
    public Message transform( TemplateMessage message )
            throws TemplateException
    {
        log.debug( "Transforming template: " + message.getTemplate().toString() );

        ReassignSimpleTemplateImpl reassignSimpleTemplate = ( ReassignSimpleTemplateImpl ) message.getTemplate();
        Message response;

        if ( reassignSimpleTemplate.getAction() == ReassignSimpleTemplateImpl.Action.CREATE )
        {
            response = createReassignSimple( message );
        }
        else if ( reassignSimpleTemplate.getAction() == ReassignSimpleTemplateImpl.Action.MODIFY )
        {
            response = modifyReassignSimple( message );
        }
        else if ( reassignSimpleTemplate.getAction() == ReassignSimpleTemplateImpl.Action.REMOVE )
        {
            response = deleteReassignSimple( message );
        }
        else
        {
            throw new TemplateException( MessageBundle.UNKNOWN_TEMPLATE_ACTION_MESSAGE );
        }

        return response;
    }

    private Message createReassignSimple( TemplateMessage message )
    {
        ReassignSimpleTemplateImpl template = ( ReassignSimpleTemplateImpl ) message.getTemplate();
        NetService netService = ServiceLocator.getNetService();
        Message response = null;

        try
        {
            // Get the parent network.
            IPRange ipRange = convertIPAddressFieldToIPRange( template.getIpAddress(), IPVersion.IPV4 );
            NetPayload parentNetPayload = netService.parentNet( ipRange.getStartIPAddress().toShortNotation(),
                    ipRange.getEndIPAddress().toShortNotation(), template.getApiKey() );

            if ( parentNetPayload != null )
            {
                NetPayload netPayload = new NetPayload();
                CustomerPayload customerPayload = new CustomerPayload();
                createPayload( template, netPayload, customerPayload );
                netPayload.setParentNetHandle( parentNetPayload.getNetHandle() );

                TicketedRequestPayload ticketedResponse = netService.reassign( parentNetPayload.getNetHandle(), template.getApiKey(), netPayload );
                log.info( "Customer handle: " + ticketedResponse.getNet().getCustomerHandle() );
                netPayload.getCustomer().setHandle( ticketedResponse.getNet().getCustomerHandle() );
                if ( ticketedResponse.getTicket() != null )
                {
                    response = Response.simpleReassignTicketed( message, ticketedResponse.getTicket(), netPayload.getCustomer() );
                }
                else
                {
                    response = Response.simpleReassignSuccessful( message, ticketedResponse.getNet(), netPayload.getCustomer() );
                }
            }
        }
        catch ( ClientResponseFailure crp )
        {
            throw new RESTResponseFailure( crp );
        }

        return response;
    }

    private Message modifyReassignSimple( TemplateMessage message )
    {
        ReassignSimpleTemplateImpl template = ( ReassignSimpleTemplateImpl ) message.getTemplate();
        NetService netService = ServiceLocator.getNetService();
        Message response = null;

        try
        {
            IPRange ipRange = convertIPAddressFieldToIPRange( template.getIpAddress(), IPVersion.IPV4 );

            NetPayload netPayload = netService.mostSpecificNet( ipRange.getStartIPAddress().toShortNotation(),
                    ipRange.getEndIPAddress().toShortNotation(), template.getApiKey() );

            confirmAppropriateNetType( netPayload );

            CustomerService customerService = ServiceLocator.getCustomerService();
            CustomerPayload currentCustomerPayload = customerService.get( netPayload.getCustomerHandle(), template.getApiKey() );

            CustomerPayload newCustomerPayload = new CustomerPayload();
            newCustomerPayload.setHandle( currentCustomerPayload.getHandle() );
            newCustomerPayload.setRegDate( currentCustomerPayload.getRegDate() );

            modifyPayload( template, netPayload, newCustomerPayload );

            netPayload = netService.modify( netPayload.getNetHandle(), template.getApiKey(), netPayload );

            response = Response.simpleReassignSuccessful( message, netPayload, newCustomerPayload );
        }
        catch ( ClientResponseFailure crp )
        {
            throw new RESTResponseFailure( crp );
        }

        return response;
    }

    private Message deleteReassignSimple( TemplateMessage message )
    {
        ReassignSimpleTemplateImpl template = ( ReassignSimpleTemplateImpl ) message.getTemplate();
        NetService netService = ServiceLocator.getNetService();
        Message response = null;

        try
        {
            IPRange ipRange = convertIPAddressFieldToIPRange( template.getIpAddress(), IPVersion.IPV4 );

            NetPayload netPayload = netService.mostSpecificNet( ipRange.getStartIPAddress().toShortNotation(),
                    ipRange.getEndIPAddress().toShortNotation(), template.getApiKey() );
            confirmAppropriateNetType( netPayload );

            setAttachmentsAndAdditionalInfo( netPayload, template );

            TicketedRequestPayload ticketedResponse = netService.remove( netPayload.getNetHandle(), template.getApiKey(), netPayload );

            if ( ticketedResponse.getTicket() != null )
            {
                response = Response.simpleReassignTicketed( message, ticketedResponse.getTicket(), null );
            }
            else
            {
                response = Response.simpleReassignSuccessful( message, ticketedResponse.getNet(), null );
            }
        }
        catch ( ClientResponseFailure crp )
        {
            throw new RESTResponseFailure( crp );
        }

        return response;
    }

    public static void createPayload( ReassignSimpleTemplateImpl template, NetPayload netPayload, CustomerPayload customerPayload )
    {
        modifyPayload( template, netPayload, customerPayload );

        if ( StringUtils.isNotEmpty( template.getIpAddress() ) )
        {
            IPRange ipRange = convertIPAddressFieldToIPRange( template.getIpAddress(), IPVersion.IPV4 );
            NetBlockPayload nbp = ipRangeToNetBlockPayloadNoCidr( ipRange );
            log.debug( "start_ip=" + nbp.getStartAddress() + " end_ip=" + nbp.getEndAddress() );
            netPayload.setNetBlocks( Collections.singletonList( nbp ) );
            netPayload.setNetTypeOnBlocks( NetBlockPayload.NetType.REASSIGNED );
        }

        setAttachmentsAndAdditionalInfo( netPayload, template );
    }

    public static void modifyPayload( ReassignSimpleTemplateImpl template, NetPayload netPayload, CustomerPayload customerPayload )
    {
        if ( isNone( template.getNetName() ) )
        {
            netPayload.setNetName( null );
        }
        else if ( StringUtils.isNotEmpty( template.getNetName() ) )
        {
            netPayload.setNetName( template.getNetName() );
        }

        TransformerAssistant.transformOriginAses( template, netPayload );

        if ( isNone( template.getCustomerName() ) )
        {
            customerPayload.setName( null );
        }
        else if ( isPrivateCustomer( template.getCustomerName() ) )
        {
            customerPayload.setName( template.getCustomerName() );
            customerPayload.setPrivateCustomer( true );
        }
        else if ( StringUtils.isNotEmpty( template.getCustomerName() ) )
        {
            customerPayload.setName( template.getCustomerName() );
            customerPayload.setPrivateCustomer( false );
        }

        if ( Y.equalsIgnoreCase( template.getPrivateCustomer() ) )
        {
            customerPayload.setPrivateCustomer( true );
        }
        else
        {
            customerPayload.setPrivateCustomer( false );
        }
        if ( template.getAddress().size() > 0 )
        {
            String address = StringUtils.join( template.getAddress(), "\n" );
            address = ( address.equals( "" ) || address.equals( "\n" ) ) ? null : address.trim();
            customerPayload.setStreet( address );
        }

        if ( isNone( template.getCity() ) )
        {
            customerPayload.setCity( null );
        }
        else if ( StringUtils.isNotEmpty( template.getCity() ) )
        {
            customerPayload.setCity( template.getCity() );
        }

        if ( isNone( template.getStateProvince() ) )
        {
            customerPayload.setStateProvince( null );
        }
        else if ( StringUtils.isNotEmpty( template.getStateProvince() ) )
        {
            customerPayload.setStateProvince( normalizeStateProv( template.getStateProvince() ) );
        }

        if ( isNone( template.getPostalCode() ) )
        {
            customerPayload.setPostalCode( null );
        }
        else if ( StringUtils.isNotEmpty( template.getPostalCode() ) )
        {
            customerPayload.setPostalCode( template.getPostalCode() );
        }

        if ( isNone( template.getCountryCode() ) )
        {
            customerPayload.setCountry( null );
        }
        else if ( StringUtils.isNotEmpty( template.getCountryCode() ) )
        {
            customerPayload.setCountry( new CountryPayload( template.getCountryCode() ) );
        }

        netPayload.setCustomer( customerPayload );

        TransformerAssistant.transformPublicComments( template, netPayload );
    }

    private static Boolean isPrivateCustomer( String string )
    {
        string = ( string == null ) ? "" : string.trim();
        String uppercaseString = StringUtils.upperCase( string );
        return uppercaseString.contains( PRIVATE_CUSTOMER );
    }

    /**
     * For either a MODIFY or REMOVE action, the REASSIGN-SIMPLE template may act only on a simple reassign. For other
     * networks, the user should send in a NET-MOD template instead.
     *
     * @param payload net payload being acted upon
     */
    private void confirmAppropriateNetType( NetPayload payload )
    {
        // If the net payload does not represent simple reassign, throw an error. Simple Reassign is a net of type
        // 'REASSIGNED' assigned to a Customer (i.e. NOT an Org).
        if ( !NetBlockPayload.NetType.REASSIGNED.equals( payload.getNetType() )
                || StringUtils.isNotEmpty( payload.getOrgHandle() ) )
        {
            throw new TemplateException( MessageBundle.SIMPLE_REASSIGN_INVALID_MODIFY );
        }
    }
}
