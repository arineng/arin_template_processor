package net.arin.tp.processor.transform;

import net.arin.tp.api.payload.AttachmentPayload;
import net.arin.tp.api.payload.CountryPayload;
import net.arin.tp.api.payload.MessagePayload;
import net.arin.tp.api.payload.OrgPayload;
import net.arin.tp.api.payload.PocLinkPayload;
import net.arin.tp.api.payload.TicketPayload;
import net.arin.tp.api.service.OrgService;
import net.arin.tp.processor.exception.RESTResponseFailure;
import net.arin.tp.processor.exception.TemplateException;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.template.Attachment;
import net.arin.tp.processor.template.OrgTemplate;
import net.arin.tp.processor.template.OrgTemplateImpl;
import net.arin.tp.processor.utils.MessageBundle;
import net.arin.tp.processor.utils.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.ClientResponseFailure;

import javax.mail.Message;
import java.util.ArrayList;
import java.util.List;

public class OrgTransformer extends Transformer
{
    @Override
    public Message transform( TemplateMessage message )
            throws TemplateException
    {
        OrgTemplateImpl orgTemplate = ( OrgTemplateImpl ) message.getTemplate();

        Message response;

        if ( orgTemplate.getAction() == null )
        {
            throw new TemplateException( MessageBundle.UNKNOWN_TEMPLATE_ACTION_MESSAGE );
        }

        switch ( orgTemplate.getAction() )
        {
            case CREATE:
                response = createOrg( message );
                break;
            case MODIFY:
                response = modifyOrg( message );
                break;
            case REMOVE:
                response = removeOrg( message );
                break;
            default:
                throw new TemplateException( MessageBundle.UNKNOWN_TEMPLATE_ACTION_MESSAGE );
        }

        return response;
    }

    private Message createOrg( TemplateMessage message ) throws RESTResponseFailure
    {
        OrgTemplateImpl template = ( OrgTemplateImpl ) message.getTemplate();
        OrgPayload orgPayload = createPayload( template );
        OrgService service = ServiceLocator.getOrgService();

        try
        {
            TicketPayload result = service.create( template.getApiKey(), orgPayload );
            return Response.createOrgSuccessful( message, result );
        }
        catch ( ClientResponseFailure crf )
        {
            throw new RESTResponseFailure( crf );
        }
    }

    private Message modifyOrg( TemplateMessage message ) throws RESTResponseFailure
    {
        OrgTemplateImpl template = ( OrgTemplateImpl ) message.getTemplate();
        OrgService service = ServiceLocator.getOrgService();

        try
        {
            OrgPayload orgPayload = service.get( template.getOrgHandle(), template.getApiKey() );
            OrgPayload updatedOrgPayload = modifyPayload( template, orgPayload );
            OrgPayload result = service.modify( template.getOrgHandle(), template.getApiKey(), updatedOrgPayload );
            return Response.orgSuccessful( message, result );
        }
        catch ( ClientResponseFailure crf )
        {
            throw new RESTResponseFailure( crf );
        }
    }

    private Message removeOrg( TemplateMessage message ) throws RESTResponseFailure
    {
        OrgTemplateImpl template = ( OrgTemplateImpl ) message.getTemplate();
        OrgService service = ServiceLocator.getOrgService();

        try
        {
            OrgPayload result = service.delete( template.getOrgHandle(), template.getApiKey() );

            return Response.orgSuccessful( message, result );
        }
        catch ( ClientResponseFailure crf )
        {
            throw new RESTResponseFailure( crf );
        }
    }

    /**
     * Converts the {@link net.arin.tp.processor.template.OrgTemplateImpl} to the
     * {@link net.arin.tp.api.payload.OrgPayload} used in the RESTful calls.
     *
     * @param template the ORG template to convert
     * @return the OrgPayload that can be used in RESTful calls
     */
    public static OrgPayload createPayload( OrgTemplateImpl template )
    {
        OrgPayload payload = new OrgPayload();

        payload.setStateProvince( normalizeStateProv( template.getState() ) );
        payload.setOrgHandle( template.getOrgHandle() );
        payload.setOrgName( template.getLegalName() );
        payload.setDbaName( template.getDba() );
        payload.setTaxId( template.getTaxId() );
        payload.setStreet( StringUtils.join( template.getAddress(), "\n" ).trim() );
        payload.setCity( template.getCity() );
        payload.setPostalCode( template.getPostalCode() );
        payload.setCountry( new CountryPayload( template.getCountryCode() ) );

        payload.getPocLinks().clear();

        // Only one admin POC.
        if ( StringUtils.isNotEmpty( template.getAdminPocHandle() ) )
        {
            payload.setAdminPoc( template.getAdminPocHandle() );
        }

        for ( String techPoc : template.getTechPocHandles() )
        {
            payload.addTechPoc( techPoc );
        }

        for ( String abusePoc : template.getAbusePocHandles() )
        {
            payload.addAbusePoc( abusePoc );
        }

        for ( String nocPoc : template.getNocPocHandles() )
        {
            payload.addNocPoc( nocPoc );
        }

        payload.setOrgUrl( template.getReferralServer() );

        payload.setPublicComments( StringUtils.join( template.getPublicComments(), "\n" ) );

        setAttachmentsAndAdditionalInfo( payload, template );

        return payload;
    }

    private static void setAttachmentsAndAdditionalInfo( OrgPayload payload, OrgTemplateImpl template )
    {
        List<Attachment> attachments = template.getAttachments();
        List<String> additionalInfo = template.getAdditionalInfo();

        if ( attachments.size() > 0
                || ( additionalInfo != null && additionalInfo.size() != 0
                && StringUtils.join( additionalInfo, "" ).trim().length() > 0 ) )
        {
            String text = StringUtils.join( additionalInfo, "\n" );
            if ( text == null || text.trim().length() <= 0 )
            {
                text = "";
            }
            MessagePayload messagePayload = MessagePayload.createAdditionalInformation( text );
            messagePayload.setCategory( MessagePayload.Category.JUSTIFICATION );

            int unnamed = 0;
            for ( Attachment attachment : attachments )
            {
                AttachmentPayload attachmentPayload = new AttachmentPayload();
                if ( StringUtils.isEmpty( attachment.getFilename() ) )
                {
                    unnamed++;
                    attachmentPayload.setFilename( "unnamed" + unnamed + ".txt" );
                }
                else
                {
                    attachmentPayload.setFilename( attachment.getFilename() );
                }
                attachmentPayload.setData( attachment.getData() );
                messagePayload.addAttachment( attachmentPayload );
            }

            if ( payload.getMessages() == null )
            {
                payload.setMessages( new ArrayList<>() );
            }
            payload.getMessages().add( messagePayload );
        }
    }

    public static OrgPayload modifyPayload( OrgTemplate template, OrgPayload payload )
    {
        if ( isNone( template.getState() ) )
        {
            payload.setStateProvince( null );
        }
        else if ( !StringUtils.isEmpty( template.getState() ) )
        {
            payload.setStateProvince( normalizeStateProv( template.getState() ) );
        }

        if ( isNone( template.getOrgHandle() ) )
        {
            payload.setOrgHandle( null );
        }
        else if ( !StringUtils.isEmpty( template.getOrgHandle() ) )
        {
            payload.setOrgHandle( template.getOrgHandle() );
        }

        if ( isNone( template.getLegalName() ) )
        {
            payload.setOrgName( null );
        }
        else if ( !StringUtils.isEmpty( template.getLegalName() ) )
        {
            payload.setOrgName( template.getLegalName() );
        }

        if ( isNone( template.getDba() ) )
        {
            payload.setDbaName( null );
        }
        else if ( !StringUtils.isEmpty( template.getDba() ) )
        {
            payload.setDbaName( template.getDba() );
        }

        if ( isNone( template.getTaxId() ) )
        {
            payload.setTaxId( null );
        }
        else if ( !StringUtils.isEmpty( template.getTaxId() ) )
        {
            payload.setTaxId( template.getTaxId() );
        }

        if ( template.getAddress().size() > 0 && hasNone( template.getAddress() ) )
        {
            payload.setStreet( null );
        }
        else if ( template.getAddress().size() > 0 )
        {
            String address = StringUtils.join( template.getAddress(), "\n" );
            address = ( address.equals( "" ) || address.equals( "\n" ) ) ? null : address.trim();
            payload.setStreet( address );
        }

        if ( isNone( template.getCity() ) )
        {
            payload.setCity( null );
        }
        else if ( !StringUtils.isEmpty( template.getCity() ) )
        {
            payload.setCity( template.getCity() );
        }

        if ( isNone( template.getPostalCode() ) )
        {
            payload.setPostalCode( null );
        }
        else if ( !StringUtils.isEmpty( template.getPostalCode() ) )
        {
            payload.setPostalCode( template.getPostalCode() );
        }

        if ( isNone( template.getCountryCode() ) )
        {
            payload.setCountry( null );
        }
        else if ( !StringUtils.isEmpty( template.getCountryCode() ) )
        {
            payload.setCountry( new CountryPayload( template.getCountryCode() ) );
        }

        if ( isNone( template.getAdminPocHandle() ) )
        {
            payload.clearAdminPoc();
        }
        else if ( !StringUtils.isEmpty( template.getAdminPocHandle() ) )
        {
            payload.setAdminPoc( template.getAdminPocHandle() );
        }

        TransformerAssistant.transformPocs( PocLinkPayload.Function.T, payload, template.getTechPocHandles(),
                OrgTemplateImpl.TECH_POC_HANDLE_LABEL );
        TransformerAssistant.transformPocs( PocLinkPayload.Function.AB, payload, template.getAbusePocHandles(),
                OrgTemplateImpl.ABUSE_POC_HANDLE_LABEL );
        TransformerAssistant.transformPocs( PocLinkPayload.Function.N, payload, template.getNocPocHandles(),
                OrgTemplateImpl.NOC_POC_HANDLE_LABEL );

        // Referral servers treated the same way as POCs.
        if ( isNone( template.getReferralServer() ) )
        {
            payload.setOrgUrl( null );
        }
        else if ( !StringUtils.isEmpty( template.getReferralServer() ) )
        {
            payload.setOrgUrl( template.getReferralServer() );
        }

        TransformerAssistant.transformPublicComments( template, payload );

        return payload;
    }
}
