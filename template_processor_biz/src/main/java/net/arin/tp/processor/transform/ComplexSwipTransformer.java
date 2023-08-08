package net.arin.tp.processor.transform;

import net.arin.tp.ipaddr.IPRange;
import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.OrgPayload;
import net.arin.tp.api.payload.TicketedRequestPayload;
import net.arin.tp.api.service.NetService;
import net.arin.tp.api.service.OrgService;
import net.arin.tp.processor.exception.EmbeddedOrgRESTResponseFailure;
import net.arin.tp.processor.exception.RESTResponseFailure;
import net.arin.tp.processor.exception.TemplateException;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.template.ComplexSwipTemplateImpl;
import net.arin.tp.processor.template.OrgTemplateSwipEmbeddedImpl;
import net.arin.tp.processor.template.PocTemplateSwipEmbeddedImpl;
import net.arin.tp.processor.utils.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.ClientResponseFailure;

import javax.mail.Message;
import java.util.ArrayList;

public abstract class ComplexSwipTransformer extends NetTransformer
{
    abstract TicketedRequestPayload performSwip( NetService netService, String parentNetHandle, String apiKey, NetPayload net );

    abstract Message setupUnticketedResponse( TemplateMessage message, NetPayload net, OrgPayload org );

    /**
     * Main method for processing the reallocate or detailed reassign templates.
     *
     * @param message template message to process
     * @throws net.arin.tp.processor.exception.TemplateException template exception
     */
    @Override
    public Message transform( TemplateMessage message )
            throws TemplateException
    {
        ComplexSwipTemplateImpl template = ( ComplexSwipTemplateImpl ) message.getTemplate();
        Message response;

        log.debug( "Transforming " + template.getTemplateName() + " template: " + template.toString() );

        checkDownstreamOrgHandle( template );

        NetService netService = ServiceLocator.getNetService();

        try
        {
            // Convert the IP address into an IPRange for easier processing.
            IPRange ipRange = convertIPAddressFieldToIPRange( template.getIpAddress(), template.getIPVersion() );

            // Using the IP address supplied by the template, we'll load the parent network.
            NetPayload parentNetPayload = netService.parentNet( ipRange.getStartIPAddress().toShortNotation(),
                    ipRange.getEndIPAddress().toShortNotation(), template.getApiKey() );

            // Get OrgPayload.
            OrgPayload orgPayload;
            try
            {
                OrgService orgService = ServiceLocator.getOrgService();
                orgPayload = orgService.get( template.getEmbeddedOrg().getOrgHandle(), template.getApiKey() );
            }
            catch ( ClientResponseFailure crf )
            {
                throw new EmbeddedOrgRESTResponseFailure( crf );
            }

            // Construct NetPayload.
            NetPayload netPayload = new NetPayload();
            netPayload.setParentNetHandle( parentNetPayload.getNetHandle() );
            netPayload.setOrgHandle( orgPayload.getOrgHandle() );
            netPayload.setNetName( template.getNetName() );
            netPayload.setNetBlocks( new ArrayList<>() );
            netPayload.getNetBlocks().add( ipRangeToNetBlockPayloadNoCidr( ipRange ) );
            netPayload.setOriginAses( template.getOriginAses() );
            if ( template.getPublicComments() != null )
            {
                netPayload.setPublicComments( StringUtils.join( template.getPublicComments(), "\n" ) );
            }
            setAttachmentsAndAdditionalInfo( netPayload, template );

            // Now perform reallocate or detailed reassign.
            TicketedRequestPayload ticketedRequestPayload = performSwip( netService, parentNetPayload.getNetHandle(),
                    template.getApiKey(), netPayload );
            response = setupUnticketedResponse( message, ticketedRequestPayload.getNet(), orgPayload );
        }
        catch ( ClientResponseFailure crf )
        {
            throw new RESTResponseFailure( crf );
        }

        return response;
    }

    private void checkDownstreamOrgHandle( ComplexSwipTemplateImpl template )
            throws TemplateException
    {
        OrgTemplateSwipEmbeddedImpl embeddedOrg = template.getEmbeddedOrg();
        if ( StringUtils.isBlank( embeddedOrg.getOrgHandle() ) )
        {
            String error;
            PocTemplateSwipEmbeddedImpl embeddedPoc = template.getEmbeddedPoc();
            if ( StringUtils.isNotBlank( embeddedOrg.getLegalName() )
                    || !embeddedOrg.getAddress().isEmpty()
                    || StringUtils.isNotBlank( embeddedOrg.getCity() )
                    || StringUtils.isNotBlank( embeddedOrg.getState() )
                    || StringUtils.isNotBlank( embeddedOrg.getPostalCode() )
                    || StringUtils.isNotBlank( embeddedOrg.getCountryCode() )
                    || StringUtils.isNotBlank( embeddedOrg.getAdminPocHandle() )
                    || StringUtils.isNotBlank( embeddedPoc.getPocContactType() )
                    || StringUtils.isNotBlank( embeddedPoc.getLastName() )
                    || StringUtils.isNotBlank( embeddedPoc.getFirstName() )
                    || StringUtils.isNotBlank( embeddedPoc.getCompanyName() )
                    || !embeddedPoc.getAddress().isEmpty()
                    || StringUtils.isNotBlank( embeddedPoc.getCity() )
                    || StringUtils.isNotBlank( embeddedPoc.getStateProvince() )
                    || StringUtils.isNotBlank( embeddedPoc.getPostalCode() )
                    || StringUtils.isNotBlank( embeddedPoc.getCountryCode() )
                    || !embeddedPoc.getOfficePhone().isEmpty()
                    || !embeddedPoc.getEmail().isEmpty() )
            {
                error = String.format( "%s You cannot create Org or POC records for downstream customers. Please supply an Org ID from Whois.",
                        OrgTemplateSwipEmbeddedImpl.ORG_HANDLE_LABEL );
            }
            else
            {
                error = String.format( "%s Value required.", OrgTemplateSwipEmbeddedImpl.ORG_HANDLE_LABEL );
            }
            throw new TemplateException( error );
        }
    }
}
