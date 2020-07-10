package net.arin.tp.processor.template;

import net.arin.tp.api.payload.OrgPayload;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrgTemplateSwipEmbeddedImpl extends AbstractOrgTemplate
{
    public static final String ORG_HANDLE_LABEL = "Downstream Org ID:";
    private static final String LEGAL_NAME_LABEL = "Org Name:";
    private static final String ADDRESS_LABEL = "Org Address:";
    private static final String CITY_LABEL = "Org City:";
    private static final String STATE_LABEL = "Org State/Province:";
    private static final String POSTAL_CODE_LABEL = "Org Postal Code:";
    private static final String COUNTRY_CODE_LABEL = "Org Country Code:";
    private static final String ORG_POC_HANDLE_LABEL = "Org POC Handle:";

    private String orgPocHandle;

    @Override
    protected void mapKeyValuePair( String key, String value )
    {
        if ( normalizedKeyCompare( ORG_HANDLE_LABEL, key ) )
        {
            orgHandle = value.toUpperCase();
        }
        else if ( normalizedKeyCompare( LEGAL_NAME_LABEL, key ) )
        {
            legalName = value;
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
            state = value;
        }
        else if ( normalizedKeyCompare( POSTAL_CODE_LABEL, key ) )
        {
            postalCode = value;
        }
        else if ( normalizedKeyCompare( COUNTRY_CODE_LABEL, key ) )
        {
            countryCode = value;
        }
        else if ( normalizedKeyCompare( ORG_POC_HANDLE_LABEL, key ) )
        {
            orgPocHandle = value.toUpperCase();
        }
    }

    @Override
    protected boolean isMultiLineField( String fieldName )
    {
        return Objects.equals( normalizeKey( ADDRESS_LABEL ), fieldName );
    }

    @Override
    public Map<String, String> getXmlToFieldKeyMapping()
    {
        Map<String, String> mapping = new HashMap<>();

        mapping.put( OrgPayload.FIELD_ORGHANDLE, ORG_HANDLE_LABEL );
        mapping.put( OrgPayload.FIELD_ORGNAME, LEGAL_NAME_LABEL );
        mapping.put( OrgPayload.FIELD_STREET, ADDRESS_LABEL );
        mapping.put( OrgPayload.FIELD_CITY, CITY_LABEL );
        mapping.put( OrgPayload.FIELD_STATE, STATE_LABEL );
        mapping.put( OrgPayload.FIELD_POSTALCODE, POSTAL_CODE_LABEL );
        mapping.put( OrgPayload.FIELD_COUNTRY, COUNTRY_CODE_LABEL );
        mapping.put( OrgPayload.FIELD_POCLINK, ORG_POC_HANDLE_LABEL );

        return mapping;
    }

    @Override
    protected List<String> getFieldKeyValues()
    {
        return Arrays.asList( ORG_HANDLE_LABEL, LEGAL_NAME_LABEL, ADDRESS_LABEL, CITY_LABEL, STATE_LABEL,
                POSTAL_CODE_LABEL, COUNTRY_CODE_LABEL, ORG_POC_HANDLE_LABEL );
    }

    @Override
    public String getAdminPocHandle()
    {
        return orgPocHandle;
    }

    @Override
    public List<String> getTechPocHandles()
    {
        LinkedList<String> techPocList = new LinkedList<>();

        techPocList.add( orgPocHandle );

        return techPocList;
    }

    /**
     * Embedded organizations do not support the following fields:
     */

    @Override
    public void setAdminPocHandle( String adminPocHandle )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTechPocHandles( List<String> techPocHandles )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDba( String dba )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTaxId( String taxId )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAbusePocHandles( List<String> abusePocHandles )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNocPocHandles( List<String> nocPocHandles )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setReferralServer( String referralServer )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPublicComments( List<String> publicComments )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTemplateVersion()
    {
        return null;
    }

    @Override
    public String getTemplateName()
    {
        return null;
    }
}
