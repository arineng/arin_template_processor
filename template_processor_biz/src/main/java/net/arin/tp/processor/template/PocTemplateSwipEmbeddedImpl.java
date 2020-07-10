package net.arin.tp.processor.template;

import com.google.common.collect.Collections2;
import net.arin.tp.api.payload.PocPayload;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PocTemplateSwipEmbeddedImpl extends AbstractPocTemplate
{
    private static final String POC_HANDLE_LABEL = "Org POC Handle:";
    private static final String POC_CONTACT_TYPE_LABEL = "Org POC Contact Type (P or R):";
    private static final String POC_LAST_NAME_LABEL = "Org POC Last Name or Role Account:";
    private static final String POC_FIRST_NAME_LABEL = "Org POC First Name:";
    private static final String POC_COMPANY_LABEL = "Org POC Company Name:";
    private static final String POC_ADDRESS_LABEL = "Org POC Address:";
    private static final String POC_CITY_LABEL = "Org POC City:";
    private static final String POC_STATE_LABEL = "Org POC State/Province:";
    private static final String POC_POSTAL_CODE_LABEL = "Org POC Postal Code:";
    private static final String POC_COUNTRY_CODE_LABEL = "Org POC Country Code:";
    private static final String POC_OFFICE_PHONE_LABEL = "Org POC Office Phone Number:";
    private static final String POC_EMAIL_ADDRESS_LABEL = "Org POC E-mail Address:";

    @Override
    protected List<String> getFieldKeyValues()
    {
        return Arrays.asList( POC_HANDLE_LABEL, POC_CONTACT_TYPE_LABEL, POC_LAST_NAME_LABEL, POC_FIRST_NAME_LABEL,
                POC_COMPANY_LABEL, POC_ADDRESS_LABEL, POC_CITY_LABEL, POC_STATE_LABEL, POC_POSTAL_CODE_LABEL,
                POC_COUNTRY_CODE_LABEL, POC_OFFICE_PHONE_LABEL, POC_EMAIL_ADDRESS_LABEL );
    }

    @Override
    public Map<String, String> getXmlToFieldKeyMapping()
    {
        Map<String, String> mapping = new HashMap<>();

        mapping.put( PocPayload.FIELD_POCHANDLE, POC_HANDLE_LABEL );
        mapping.put( PocPayload.FIELD_CONTACTTYPE, POC_CONTACT_TYPE_LABEL );
        mapping.put( PocPayload.FIELD_LASTNAME, POC_LAST_NAME_LABEL );
        mapping.put( PocPayload.FIELD_FIRSTNAME, POC_FIRST_NAME_LABEL );
        mapping.put( PocPayload.FIELD_COMPANYNAME, POC_COMPANY_LABEL );
        mapping.put( PocPayload.FIELD_STREET, POC_ADDRESS_LABEL );
        mapping.put( PocPayload.FIELD_CITY, POC_CITY_LABEL );
        mapping.put( PocPayload.FIELD_STATE, POC_STATE_LABEL );
        mapping.put( PocPayload.FIELD_POSTALCODE, POC_POSTAL_CODE_LABEL );
        mapping.put( PocPayload.FIELD_COUNTRY, POC_COUNTRY_CODE_LABEL );
        mapping.put( PocPayload.FIELD_OFFICEPHONE, POC_OFFICE_PHONE_LABEL );
        mapping.put( PocPayload.FIELD_EMAIL, POC_EMAIL_ADDRESS_LABEL );

        return mapping;
    }

    @Override
    protected boolean isMultiLineField( String fieldName )
    {
        if ( Collections2.transform(
                Arrays.asList( POC_ADDRESS_LABEL, POC_OFFICE_PHONE_LABEL, POC_EMAIL_ADDRESS_LABEL ),
                normalizeKeyFunction ).contains( fieldName ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void mapKeyValuePair( String key, String value )
    {
        if ( normalizedKeyCompare( POC_HANDLE_LABEL, key ) )
        {
            pocHandle = value.toUpperCase();
        }
        else if ( normalizedKeyCompare( POC_CONTACT_TYPE_LABEL, key ) )
        {
            type = value;
        }
        else if ( normalizedKeyCompare( POC_LAST_NAME_LABEL, key ) )
        {
            lastName = value;
        }
        else if ( normalizedKeyCompare( POC_FIRST_NAME_LABEL, key ) )
        {
            firstName = value;
        }
        else if ( normalizedKeyCompare( POC_COMPANY_LABEL, key ) )
        {
            companyName = value;
        }
        else if ( normalizedKeyCompare( POC_ADDRESS_LABEL, key ) )
        {
            address.add( value );
        }
        else if ( normalizedKeyCompare( POC_CITY_LABEL, key ) )
        {
            city = value;
        }
        else if ( normalizedKeyCompare( POC_STATE_LABEL, key ) )
        {
            stateProvince = value;
        }
        else if ( normalizedKeyCompare( POC_POSTAL_CODE_LABEL, key ) )
        {
            postalCode = value;
        }
        else if ( normalizedKeyCompare( POC_COUNTRY_CODE_LABEL, key ) )
        {
            countryCode = value;
        }
        else if ( normalizedKeyCompare( POC_OFFICE_PHONE_LABEL, key ) )
        {
            officePhone.add( value );
        }
        else if ( normalizedKeyCompare( POC_EMAIL_ADDRESS_LABEL, key ) )
        {
            email.add( value );
        }
    }

    /**
     * The following fields aren't used in the embedded POC template:
     */

    @Override
    public void setOfficePhoneExt( List<String> officePhoneExt )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMiddleName( String middleName )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMobilePhone( List<String> mobilePhone )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFaxPhone( List<String> faxPhone )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPublicComments( List<String> pubComment )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTemplateName()
    {
        return null;
    }

    @Override
    public String getTemplateVersion()
    {
        return null;
    }
}
