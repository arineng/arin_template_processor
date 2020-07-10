package net.arin.tp.processor.template;

import com.google.common.collect.Collections2;
import net.arin.tp.api.payload.PocPayload;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PocTemplateImpl extends AbstractPocTemplate
{
    private static final String TEMPLATE_NAME = "ARIN-POC";

    private static final String ACTION_LABEL = "Registration Action (N,M, or R):";
    public static final String POC_HANDLE_LABEL = "Existing POC Handle:";
    private static final String CONTACT_TYPE_LABEL = "Contact Type (P or R):";
    private static final String LAST_NAME_LABEL = "Last Name or Role Account:";
    private static final String FIRST_NAME_LABEL = "First Name:";
    private static final String MIDDLE_NAME_LABEL = "Middle Name:";
    private static final String COMPANY_NAME_LABEL = "Company Name:";
    private static final String ADDRESS_LABEL = "Address:";
    private static final String CITY_LABEL = "City:";
    private static final String STATE_LABEL = "State/Province:";
    private static final String POSTAL_CODE_LABEL = "Postal Code:";
    private static final String COUNTRY_CODE_LABEL = "Country Code:";
    private static final String OFFICE_PHONE_LABEL = "Office Phone Number:";
    private static final String OFFICE_PHONE_EXT_LABEL = "Office Phone Number Extension:";
    private static final String EMAIL_LABEL = "E-mail Address:";
    private static final String MOBILE_PHONE_LABEL = "Mobile:";
    private static final String FAX_PHONE_LABEL = "Fax:";
    private static final String PUBLIC_COMMENTS_LABEL = "Public Comments:";
    private static final String ADDITIONAL_INFO_LABEL = "Additional Information:";

    protected Action action;
    private String additionalInfo;

    @Override
    public Map<String, String> getXmlToFieldKeyMapping()
    {
        Map<String, String> mapping = new HashMap<>();

        mapping.put( PocPayload.FIELD_POCHANDLE, POC_HANDLE_LABEL );
        mapping.put( PocPayload.FIELD_CONTACTTYPE, CONTACT_TYPE_LABEL );
        mapping.put( PocPayload.FIELD_LASTNAME, LAST_NAME_LABEL );
        mapping.put( PocPayload.FIELD_FIRSTNAME, FIRST_NAME_LABEL );
        mapping.put( PocPayload.FIELD_MIDDLENAME, MIDDLE_NAME_LABEL );
        mapping.put( PocPayload.FIELD_COMPANYNAME, COMPANY_NAME_LABEL );
        mapping.put( PocPayload.FIELD_STREET, ADDRESS_LABEL );
        mapping.put( PocPayload.FIELD_CITY, CITY_LABEL );
        mapping.put( PocPayload.FIELD_STATE, STATE_LABEL );
        mapping.put( PocPayload.FIELD_POSTALCODE, POSTAL_CODE_LABEL );
        mapping.put( PocPayload.FIELD_COUNTRY, COUNTRY_CODE_LABEL );
        mapping.put( PocPayload.FIELD_OFFICEPHONE, OFFICE_PHONE_LABEL );
        mapping.put( PocPayload.FIELD_MOBILEPHONE, MOBILE_PHONE_LABEL );
        mapping.put( PocPayload.FIELD_FAXPHONE, FAX_PHONE_LABEL );
        mapping.put( PocPayload.FIELD_EMAIL, EMAIL_LABEL );
        mapping.put( PocPayload.FIELD_PUBLICCOMMENTS, PUBLIC_COMMENTS_LABEL );

        return mapping;
    }

    @Override
    protected List<String> getFieldKeyValues()
    {
        return Arrays.asList( ACTION_LABEL, POC_HANDLE_LABEL, CONTACT_TYPE_LABEL, LAST_NAME_LABEL, FIRST_NAME_LABEL,
                MIDDLE_NAME_LABEL, COMPANY_NAME_LABEL, ADDRESS_LABEL, CITY_LABEL, STATE_LABEL, POSTAL_CODE_LABEL,
                COUNTRY_CODE_LABEL, OFFICE_PHONE_LABEL, OFFICE_PHONE_EXT_LABEL, EMAIL_LABEL, MOBILE_PHONE_LABEL,
                FAX_PHONE_LABEL, PUBLIC_COMMENTS_LABEL, ADDITIONAL_INFO_LABEL );
    }

    @Override
    protected boolean isMultiLineField( String field )
    {
        if ( Collections2.transform(
                Arrays.asList(
                        ACTION_LABEL, POC_HANDLE_LABEL, CONTACT_TYPE_LABEL, LAST_NAME_LABEL, FIRST_NAME_LABEL,
                        MIDDLE_NAME_LABEL, COMPANY_NAME_LABEL, CITY_LABEL, STATE_LABEL, POSTAL_CODE_LABEL,
                        COUNTRY_CODE_LABEL ), normalizeKeyFunction ).contains( field ) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String getTemplateName()
    {
        return TEMPLATE_NAME;
    }

    protected void mapKeyValuePair( String key, String value )
    {
        if ( normalizedKeyCompare( ACTION_LABEL, key ) )
        {
            action = Action.fromTemplate( value );
        }
        else if ( normalizedKeyCompare( POC_HANDLE_LABEL, key ) )
        {
            pocHandle = value;
        }
        else if ( normalizedKeyCompare( CONTACT_TYPE_LABEL, key ) )
        {
            type = value;
        }
        else if ( normalizedKeyCompare( LAST_NAME_LABEL, key ) )
        {
            lastName = value;
        }
        else if ( normalizedKeyCompare( FIRST_NAME_LABEL, key ) )
        {
            firstName = value;
        }
        else if ( normalizedKeyCompare( MIDDLE_NAME_LABEL, key ) )
        {
            middleName = value;
        }
        else if ( normalizedKeyCompare( COMPANY_NAME_LABEL, key ) )
        {
            companyName = value;
        }
        else if ( normalizedKeyCompare( ADDRESS_LABEL, key ) )
        {
            address.add( value );
        }
        else if ( normalizedKeyCompare( CITY_LABEL, key ) )
        {
            city = value;
        }
        else if ( normalizedKeyCompare( STATE_LABEL, key ) )
        {
            stateProvince = value;
        }
        else if ( normalizedKeyCompare( POSTAL_CODE_LABEL, key ) )
        {
            postalCode = value;
        }
        else if ( normalizedKeyCompare( COUNTRY_CODE_LABEL, key ) )
        {
            countryCode = value;
        }
        else if ( normalizedKeyCompare( OFFICE_PHONE_LABEL, key ) )
        {
            officePhone.add( value );
            officePhoneExt.add( "" );
        }
        else if ( normalizedKeyCompare( OFFICE_PHONE_EXT_LABEL, key ) )
        {
            if ( officePhoneExt.size() > 0 )
            {
                officePhoneExt.remove( officePhoneExt.size() - 1 );
            }
            officePhoneExt.add( value );
        }
        else if ( normalizedKeyCompare( EMAIL_LABEL, key ) )
        {
            email.add( value );
        }
        else if ( normalizedKeyCompare( MOBILE_PHONE_LABEL, key ) )
        {
            mobilePhone.add( value );
        }
        else if ( normalizedKeyCompare( FAX_PHONE_LABEL, key ) )
        {
            faxPhone.add( value );
        }
        else if ( normalizedKeyCompare( PUBLIC_COMMENTS_LABEL, key ) )
        {
            pubComment.add( value );
        }
        else if ( normalizedKeyCompare( ADDITIONAL_INFO_LABEL, key ) )
        {
            additionalInfo = value;
        }
    }

    public Action getAction()
    {
        return action;
    }

    public void setAction( Action action )
    {
        this.action = action;
    }

    public String getAdditionalInfo()
    {
        return additionalInfo;
    }

    public void setAdditionalInfo( String additionalInfo )
    {
        this.additionalInfo = additionalInfo;
    }
}
