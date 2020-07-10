package net.arin.tp.processor.template;

import com.google.common.collect.Collections2;
import net.arin.tp.api.payload.OrgPayload;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class OrgTemplateImpl extends AbstractOrgTemplate
{
    private static final String TEMPLATE_NAME = "ARIN-ORG";

    private static final String ACTION_LABEL = "Registration Action (N,M, or R):";
    public static final String ORG_HANDLE_LABEL = "Existing OrgID:";
    private static final String LEGAL_NAME_LABEL = "Organization's Legal Name:";
    private static final String ORGANIZATION_DBA_LABEL = "Organization's D/B/A:";
    private static final String TAX_ID_LABEL = "Business Tax ID Number (DO NOT LIST SSN):";
    private static final String ADDRESS_LABEL = "Org Address:";
    private static final String CITY_LABEL = "Org City:";
    private static final String STATE_LABEL = "Org State/Province:";
    private static final String POSTAL_CODE_LABEL = "Org Postal Code:";
    private static final String COUNTRY_CODE_LABEL = "Org Country Code:";
    private static final String ADMIN_POC_HANDLE_LABEL = "Admin POC Handle:";
    public static final String TECH_POC_HANDLE_LABEL = "Tech POC Handle:";
    public static final String ABUSE_POC_HANDLE_LABEL = "Abuse POC Handle:";
    public static final String NOC_POC_HANDLE_LABEL = "NOC POC Handle:";
    private static final String REFERRAL_SERVER_LABEL = "Referral Server:";
    private static final String PUBLIC_COMMENTS_LABEL = "Public Comments:";
    private static final String ADDITIONAL_INFO_LABEL = "Additional Information:";

    protected Action action;
    private List<String> additionalInfo = new LinkedList<>();

    @Override
    public String getTemplateName()
    {
        return TEMPLATE_NAME;
    }

    @Override
    protected List<String> getFieldKeyValues()
    {
        return Arrays.asList( ACTION_LABEL, ORG_HANDLE_LABEL, LEGAL_NAME_LABEL, ORGANIZATION_DBA_LABEL, TAX_ID_LABEL,
                ADDRESS_LABEL, CITY_LABEL, STATE_LABEL, POSTAL_CODE_LABEL, COUNTRY_CODE_LABEL, ADMIN_POC_HANDLE_LABEL,
                TECH_POC_HANDLE_LABEL, ABUSE_POC_HANDLE_LABEL, NOC_POC_HANDLE_LABEL, REFERRAL_SERVER_LABEL, PUBLIC_COMMENTS_LABEL,
                ADDITIONAL_INFO_LABEL );
    }

    @Override
    public Map<String, String> getXmlToFieldKeyMapping()
    {
        Map<String, String> mapping = new HashMap<>();

        mapping.put( OrgPayload.FIELD_ORGHANDLE, ORG_HANDLE_LABEL );
        mapping.put( OrgPayload.FIELD_ORGNAME, LEGAL_NAME_LABEL );
        mapping.put( OrgPayload.FIELD_DBANAME, ORGANIZATION_DBA_LABEL );
        mapping.put( OrgPayload.FIELD_TAXID, TAX_ID_LABEL );
        mapping.put( OrgPayload.FIELD_STREET, ADDRESS_LABEL );
        mapping.put( OrgPayload.FIELD_CITY, CITY_LABEL );
        mapping.put( OrgPayload.FIELD_STATE, STATE_LABEL );
        mapping.put( OrgPayload.FIELD_POSTALCODE, POSTAL_CODE_LABEL );
        mapping.put( OrgPayload.FIELD_COUNTRY, COUNTRY_CODE_LABEL );
        mapping.put( OrgPayload.FIELD_ORGURL, REFERRAL_SERVER_LABEL );
        mapping.put( OrgPayload.FIELD_PUBLICCOMMENTS, PUBLIC_COMMENTS_LABEL );

        // This is a special case since the error messages for POC links are so well-defined. We don't need to provide
        // a label for this value and we can't really determine which POC handle caused the failure anyway so this is
        // cleaner.
        mapping.put( OrgPayload.FIELD_POCLINK, "" );

        return mapping;
    }

    @Override
    protected boolean isMultiLineField( String field )
    {
        if ( Collections2.transform( Arrays.asList(
                ACTION_LABEL, ORG_HANDLE_LABEL, LEGAL_NAME_LABEL, ORGANIZATION_DBA_LABEL, TAX_ID_LABEL,
                CITY_LABEL, STATE_LABEL, POSTAL_CODE_LABEL, COUNTRY_CODE_LABEL, ADMIN_POC_HANDLE_LABEL,
                REFERRAL_SERVER_LABEL ),
                normalizeKeyFunction ).contains( field ) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    protected void mapKeyValuePair( String key, String value )
    {
        if ( normalizedKeyCompare( ACTION_LABEL, key ) )
        {
            action = Action.fromTemplate( value );
        }
        else if ( normalizedKeyCompare( ORG_HANDLE_LABEL, key ) )
        {
            orgHandle = value.toUpperCase();
        }
        else if ( normalizedKeyCompare( LEGAL_NAME_LABEL, key ) )
        {
            legalName = value;
        }
        else if ( normalizedKeyCompare( ORGANIZATION_DBA_LABEL, key ) )
        {
            dba = value;
        }
        else if ( normalizedKeyCompare( TAX_ID_LABEL, key ) )
        {
            taxId = value;
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
        else if ( normalizedKeyCompare( ADMIN_POC_HANDLE_LABEL, key ) )
        {
            adminPocHandle = value.toUpperCase();
        }
        else if ( normalizedKeyCompare( TECH_POC_HANDLE_LABEL, key ) )
        {
            techPocHandles.add( value.toUpperCase() );
        }
        else if ( normalizedKeyCompare( ABUSE_POC_HANDLE_LABEL, key ) )
        {
            abusePocHandles.add( value.toUpperCase() );
        }
        else if ( normalizedKeyCompare( NOC_POC_HANDLE_LABEL, key ) )
        {
            nocPocHandles.add( value.toUpperCase() );
        }
        else if ( normalizedKeyCompare( REFERRAL_SERVER_LABEL, key ) )
        {
            referralServer = value;
        }
        else if ( normalizedKeyCompare( PUBLIC_COMMENTS_LABEL, key ) )
        {
            publicComments.add( value );
        }
        else if ( normalizedKeyCompare( ADDITIONAL_INFO_LABEL, key ) )
        {
            additionalInfo.add( value );
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

    public List<String> getAdditionalInfo()
    {
        return additionalInfo;
    }

    public void setAdditionalInfo( List<String> additionalInfo )
    {
        this.additionalInfo = additionalInfo;
    }
}
