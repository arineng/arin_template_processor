package net.arin.tp.processor.transform;

import net.arin.tp.api.payload.CountryPayload;
import net.arin.tp.api.payload.PhonePayload;
import net.arin.tp.api.payload.PhoneTypePayload;
import net.arin.tp.api.payload.PocPayload;
import net.arin.tp.api.service.PocService;
import net.arin.tp.processor.exception.RESTResponseFailure;
import net.arin.tp.processor.exception.TemplateException;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.template.PocTemplate;
import net.arin.tp.processor.template.PocTemplateImpl;
import net.arin.tp.processor.utils.MessageBundle;
import net.arin.tp.processor.utils.ServiceLocator;
import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts an email template to RESTful calls to Reg-RWS, and sends the response back to the customer.
 */
public class PocTransformer extends Transformer
{
    private static final Logger log = LoggerFactory.getLogger( PocTransformer.class );

    private static final String P_CONTACT_TYPE = "P";
    private static final String R_CONTACT_TYPE = "R";
    private static final String PERSON_CONTACT_TYPE = "PERSON";
    private static final String ROLE_CONTACT_TYPE = "ROLE";

    /**
     * Transforms a POC Email Template to a {@link net.arin.tp.processor.template.PocTemplateImpl}.
     *
     * @param message the Template Processor message containing the email template
     */
    @Override
    public Message transform( TemplateMessage message )
            throws TemplateException
    {
        log.debug( "Transforming template: " + message.getTemplate().toString() );

        PocTemplateImpl pocTemplate = ( PocTemplateImpl ) message.getTemplate();
        Message response;

        if ( pocTemplate.getAction() == null )
        {
            throw new TemplateException( MessageBundle.UNKNOWN_TEMPLATE_ACTION_MESSAGE );
        }

        switch ( pocTemplate.getAction() )
        {
            case CREATE:
                response = createPoc( message );
                break;
            case MODIFY:
                response = modifyPoc( message );
                break;
            case REMOVE:
                response = removePoc( message );
                break;
            default:
                throw new TemplateException( MessageBundle.UNKNOWN_TEMPLATE_ACTION_MESSAGE );
        }

        return response;
    }

    private Message createPoc( TemplateMessage message )
    {
        PocTemplateImpl template = ( PocTemplateImpl ) message.getTemplate();
        PocPayload pocPayload = createPayload( template );
        PocService service = ServiceLocator.getPocService();

        try
        {
            PocPayload result = service.create( template.getApiKey(), false, pocPayload );
            log.debug( "Created POC: " + result.getPocHandle() );

            return Response.pocSuccessful( message, result );
        }
        catch ( ClientResponseFailure crp )
        {
            throw new RESTResponseFailure( crp );
        }
    }

    private Message modifyPoc( TemplateMessage message )
    {
        PocTemplateImpl template = ( PocTemplateImpl ) message.getTemplate();
        PocService service = ServiceLocator.getPocService();

        try
        {
            PocPayload pocPayload = service.get( template.getPocHandle(), template.getApiKey() );
            PocPayload updatedPocPayload = modifyPayload( template, pocPayload );
            PocPayload result = service.modify( template.getPocHandle(), template.getApiKey(), updatedPocPayload );
            log.debug( "Modified POC: " + result.getPocHandle() );

            return Response.pocSuccessful( message, result );
        }
        catch ( ClientResponseFailure crp )
        {
            throw new RESTResponseFailure( crp );
        }
    }

    private Message removePoc( TemplateMessage message )
    {
        PocTemplateImpl template = ( PocTemplateImpl ) message.getTemplate();
        PocService service = ServiceLocator.getPocService();

        try
        {
            PocPayload result = service.delete( template.getPocHandle(), template.getApiKey() );
            log.debug( "Removed POC: " + result.getPocHandle() );

            return Response.pocSuccessful( message, result );
        }
        catch ( ClientResponseFailure crp )
        {
            throw new RESTResponseFailure( crp );
        }
    }

    /**
     * Converts the {@link net.arin.tp.processor.template.PocTemplateImpl} to the
     * {@link net.arin.tp.api.payload.PocPayload} used in the RESTful calls.
     *
     * @param template the POC template to convert
     * @return the PocPayload that can be used in RESTful calls
     */
    public static PocPayload createPayload( PocTemplate template )
    {
        PocPayload payload = new PocPayload();
        payload.setContactType( convertTemplateTypeToPayloadType( template.getPocContactType() ) );
        payload.setPocHandle( template.getPocHandle() );
        payload.setLastName( template.getLastName() );
        payload.setFirstName( template.getFirstName() );
        payload.setMiddleName( template.getMiddleName() );
        payload.setCompanyName( template.getCompanyName() );
        String street = StringUtils.join( template.getAddress(), "\n" );
        payload.setStreet( street );
        payload.setCity( template.getCity() );
        payload.setState( normalizeStateProv( template.getStateProvince() ) );
        payload.setPostalCode( template.getPostalCode() );
        payload.setCountry( new CountryPayload( template.getCountryCode() ) );

        List<PhonePayload> phones = new ArrayList<>( 0 );

        if ( !template.getOfficePhone().isEmpty() )
        {
            for ( int i = 0; i < template.getOfficePhone().size(); i++ )
            {
                PhonePayload phone = new PhonePayload();
                phone.setNumber( template.getOfficePhone().get( i ) );

                if ( template.getOfficePhoneExt() != null
                        && template.getOfficePhoneExt().size() >= i + 1 )
                {
                    phone.setExtension( template.getOfficePhoneExt().get( i ) );
                }

                phone.setType( PhoneTypePayload.Code.O );
                phones.add( phone );
            }
        }

        if ( !template.getMobilePhone().isEmpty() )
        {
            for ( String mobilePhone : template.getMobilePhone() )
            {
                PhonePayload phone = new PhonePayload();
                phone.setNumber( mobilePhone );
                phone.setExtension( null );
                phone.setType( PhoneTypePayload.Code.M );
                phones.add( phone );
            }
        }

        if ( !template.getFaxPhone().isEmpty() )
        {
            for ( String faxPhone : template.getFaxPhone() )
            {
                PhonePayload phone = new PhonePayload();
                phone.setNumber( faxPhone );
                phone.setExtension( null );
                phone.setType( PhoneTypePayload.Code.F );
                phones.add( phone );
            }
        }
        payload.setPhones( phones );
        payload.setEmail( template.getEmail() );
        payload.setPublicComments( StringUtils.join( template.getPublicComments(), "\n" ) );
        return payload;
    }

    /**
     * This method applies the template information to the POC payload. If the template value is null then the payload
     * value is not altered. If the template is not null or not empty string the payload value is updated with the
     * template value. If the value of the template is NONE then the payload is updated to be null.
     */
    public static PocPayload modifyPayload( PocTemplate template, PocPayload payload )
    {

        if ( template.getPocContactType() == null || StringUtils.isEmpty( template.getPocContactType() ) )
        {
            // Do nothing.
        }
        else
        {
            payload.setContactType( convertTemplateTypeToPayloadType( template.getPocContactType() ) );
        }

        if ( isNone( template.getPocHandle() ) )
        {
            payload.setPocHandle( null );
        }
        else if ( template.getPocHandle() != null && !StringUtils.isEmpty( template.getPocHandle() ) )
        {
            payload.setPocHandle( template.getPocHandle() );
        }

        if ( isNone( template.getLastName() ) )
        {
            payload.setLastName( null );
        }
        else if ( template.getLastName() != null && !StringUtils.isEmpty( template.getLastName() ) )
        {
            payload.setLastName( template.getLastName() );
        }

        if ( isNone( template.getFirstName() ) )
        {
            payload.setFirstName( null );
        }
        else if ( template.getFirstName() != null && !StringUtils.isEmpty( template.getFirstName() ) )
        {
            payload.setFirstName( template.getFirstName() );
        }

        if ( isNone( template.getMiddleName() ) )
        {
            payload.setMiddleName( null );
        }
        else if ( template.getMiddleName() != null && !StringUtils.isEmpty( template.getMiddleName() ) )
        {
            payload.setMiddleName( template.getMiddleName() );
        }

        if ( isNone( template.getCompanyName() ) )
        {
            payload.setCompanyName( null );
        }
        else if ( template.getCompanyName() != null && !StringUtils.isEmpty( template.getCompanyName() ) )
        {
            payload.setCompanyName( template.getCompanyName() );
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
        else if ( template.getCity() != null && !StringUtils.isEmpty( template.getCity() ) )
        {
            payload.setCity( template.getCity() );
        }

        if ( isNone( template.getStateProvince() ) )
        {
            payload.setState( null );
        }
        else if ( template.getStateProvince() != null && !StringUtils.isEmpty( template.getStateProvince() ) )
        {
            payload.setState( normalizeStateProv( template.getStateProvince() ) );
        }

        if ( isNone( template.getPostalCode() ) )
        {
            payload.setPostalCode( null );
        }
        else if ( template.getPostalCode() != null && !StringUtils.isEmpty( template.getPostalCode() ) )
        {
            payload.setPostalCode( template.getPostalCode() );
        }

        if ( isNone( template.getPostalCode() ) )
        {
            payload.setPostalCode( null );
        }
        else if ( template.getPostalCode() != null && !StringUtils.isEmpty( template.getPostalCode() ) )
        {
            payload.setPostalCode( template.getPostalCode() );
        }

        if ( isNone( template.getCountryCode() ) )
        {
            payload.setCountry( null );
        }
        else if ( template.getCountryCode() != null && !StringUtils.isEmpty( template.getCountryCode() ) )
        {
            payload.setCountry( new CountryPayload( template.getCountryCode() ) );
        }

        List<PhonePayload> phones = payload.getPhones();
        if ( payload.getPhones() == null )
        {
            phones = new ArrayList<>( 0 );
        }
        modifyPayloadPhones( phones, template.getOfficePhone(), template.getOfficePhoneExt(), PhoneTypePayload.Code.O );
        modifyPayloadPhones( phones, template.getFaxPhone(), null, PhoneTypePayload.Code.F );
        modifyPayloadPhones( phones, template.getMobilePhone(), null, PhoneTypePayload.Code.M );

        payload.setPhones( phones );

        if ( template.getEmail().contains( NONE ) )
        {
            payload.setEmail( null );
        }
        else if ( template.getEmail() != null && template.getEmail().size() > 0 )
        {
            payload.setEmail( template.getEmail() );
        }

        TransformerAssistant.transformPublicComments( template, payload );

        return payload;
    }

    private static void modifyPayloadPhones( List<PhonePayload> phones, List<String> phoneList,
                                             List<String> extensionList, PhoneTypePayload.Code code )
    {
        // If the user entered something in the office phone field, update the current payload with it.
        if ( phones != null && !phoneList.isEmpty() )
        {
            if ( phones.size() != 0 )
            {
                // Remove all the office phones.
                phones.removeIf( phonePayload -> phonePayload.getType().getCode().equals( code ) );
            }
            for ( int i = 0; i < phoneList.size(); i++ )
            {
                // If the entered something other than NONE then add it.
                if ( !isNone( phoneList.get( i ) ) )
                {
                    PhonePayload phone = new PhonePayload();
                    phone.setNumber( phoneList.get( i ) );
                    if ( code.equals( PhoneTypePayload.Code.O ) )
                    {
                        phone.setExtension( extensionList.get( i ) );
                    }
                    phone.setType( code );
                    phones.add( phone );
                }
            }
        }
    }

    private static PocPayload.ContactType convertTemplateTypeToPayloadType( String type )
    {
        if ( PERSON_CONTACT_TYPE.equalsIgnoreCase( type ) || P_CONTACT_TYPE.equalsIgnoreCase( type ) )
        {
            return PocPayload.ContactType.PERSON;
        }
        if ( ROLE_CONTACT_TYPE.equalsIgnoreCase( type ) || R_CONTACT_TYPE.equalsIgnoreCase( type ) )
        {
            return PocPayload.ContactType.ROLE;
        }
        return null;
    }
}
