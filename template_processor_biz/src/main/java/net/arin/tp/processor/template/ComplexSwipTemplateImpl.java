package net.arin.tp.processor.template;

import com.google.common.collect.Collections2;
import net.arin.tp.api.payload.NetBlockPayload;
import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.service.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class ComplexSwipTemplateImpl extends NetTemplate
{
    static final String JUSTIFICATION_LABEL = "Justification:";

    // IGNORED FIELDS
    // We need to include these just so the template parsing is aware of them but they won't be used.
    private static final String NET_POC_TYPE_LABEL = "Net POC Type (T, AB, or N):";
    private static final String NET_POC_HANDLE_LABEL = "Net POC Handle:";
    private static final String NET_CONTACT_TYPE_LABEL = "Net POC Contact Type (P or R):";
    private static final String NET_LAST_NAME_LABEL = "Net POC Last Name or Role Account:";
    private static final String NET_FIRST_NAME_LABEL = "Net POC First Name:";
    private static final String NET_COMPANY_LABEL = "Net POC Company Name:";
    private static final String NET_ADDRESS_LABEL = "Net POC Address:";
    private static final String NET_CITY_LABEL = "Net POC City:";
    private static final String NET_STATE_LABEL = "Net POC State/Province:";
    private static final String NET_POSTAL_CODE_LABEL = "Net POC Postal Code:";
    private static final String NET_COUNTRY_CODE_LABEL = "Net POC Country Code:";
    private static final String NET_OFFICE_PHONE_LABEL = "Net POC Office Phone Number:";
    private static final String NET_EMAIL_ADDRESS_LABEL = "Net POC E-mail Address:";

    private List<String> justification = new LinkedList<>();

    private OrgTemplateSwipEmbeddedImpl embeddedOrg = new OrgTemplateSwipEmbeddedImpl();
    private PocTemplateSwipEmbeddedImpl embeddedPoc = new PocTemplateSwipEmbeddedImpl();

    @Override
    public Map<String, String> getXmlToFieldKeyMapping()
    {
        Map<String, String> mapping = new HashMap<>();

        mapping.put( NetPayload.FIELD_NETNAME, NET_NAME_LABEL );
        mapping.put( NetPayload.FIELD_NETHANDLE, getIpAddressLabel() );
        mapping.put( NetPayload.FIELD_START_ADDRESS, getIpAddressLabel() );
        mapping.put( NetPayload.FIELD_END_ADDRESS, getIpAddressLabel() );
        mapping.put( NetBlockPayload.FIELD_CIDR_LENGTH, getIpAddressLabel() );
        mapping.put( Parameters.PARENT_NET_HANDLE, getIpAddressLabel() );
        mapping.put( NetPayload.FIELD_ORIGIN_AS, ORIGIN_AS_LABEL );
        mapping.put( NetPayload.FIELD_ORGHANDLE, OrgTemplateSwipEmbeddedImpl.ORG_HANDLE_LABEL );
        mapping.put( NetPayload.FIELD_PUBLICCOMMENTS, PUBLIC_COMMENTS_LABEL );

        return mapping;
    }

    @Override
    protected List<String> getFieldKeyValues()
    {
        List<String> fieldKeyValues = new ArrayList<>();

        fieldKeyValues.addAll( Arrays.asList( NET_NAME_LABEL, getIpAddressLabel(), ORIGIN_AS_LABEL,
                PUBLIC_COMMENTS_LABEL, NAMESERVER_LABEL, NET_CONTACT_TYPE_LABEL, NET_LAST_NAME_LABEL,
                NET_FIRST_NAME_LABEL, NET_COMPANY_LABEL, NET_ADDRESS_LABEL, NET_CITY_LABEL, NET_STATE_LABEL,
                NET_POSTAL_CODE_LABEL, NET_COUNTRY_CODE_LABEL, NET_OFFICE_PHONE_LABEL, NET_EMAIL_ADDRESS_LABEL,
                ADDITIONAL_INFO_LABEL, NET_POC_TYPE_LABEL, NET_POC_HANDLE_LABEL ) );

        fieldKeyValues.addAll( ( new OrgTemplateSwipEmbeddedImpl() ).getFieldKeyValues() );
        fieldKeyValues.addAll( ( new PocTemplateSwipEmbeddedImpl() ).getFieldKeyValues() );

        return fieldKeyValues;
    }

    @Override
    protected boolean isMultiLineField( String fieldName )
    {
        return ( super.isMultiLineField( fieldName ) ||

                Collections2.transform(
                        Arrays.asList(
                                NET_POC_TYPE_LABEL, NET_POC_HANDLE_LABEL, NET_CONTACT_TYPE_LABEL,
                                NET_LAST_NAME_LABEL, NET_FIRST_NAME_LABEL, NET_COMPANY_LABEL, NET_ADDRESS_LABEL,
                                NET_CITY_LABEL, NET_STATE_LABEL, NET_POSTAL_CODE_LABEL, NET_COUNTRY_CODE_LABEL,
                                NET_OFFICE_PHONE_LABEL, NET_ADDRESS_LABEL, NET_EMAIL_ADDRESS_LABEL ),
                        normalizeKeyFunction ).contains( fieldName ) ||

                embeddedOrg.isMultiLineField( fieldName ) ||
                embeddedPoc.isMultiLineField( fieldName ) );
    }

    public OrgTemplateSwipEmbeddedImpl getEmbeddedOrg()
    {
        return embeddedOrg;
    }

    public void setEmbeddedOrg( OrgTemplateSwipEmbeddedImpl embeddedOrg )
    {
        this.embeddedOrg = embeddedOrg;
    }

    public PocTemplateSwipEmbeddedImpl getEmbeddedPoc()
    {
        return embeddedPoc;
    }

    public void setEmbeddedPoc( PocTemplateSwipEmbeddedImpl embeddedPoc )
    {
        this.embeddedPoc = embeddedPoc;
    }

    @Override
    protected void mapKeyValuePair( String key, String value )
    {
        // Parent covers core net fields.
        super.mapKeyValuePair( key, value );

        if ( normalizedKeyCompare( JUSTIFICATION_LABEL, key ) )
        {
            justification.add( value );
        }
        else
        {
            embeddedOrg.mapKeyValuePair( key, value );
            embeddedPoc.mapKeyValuePair( key, value );
        }
    }

    @Override
    public void setApiKey( String apiKey )
    {
        super.setApiKey( apiKey );

        // When parsing out the OrgTemplate and PocTemplate using the static transformer methods, it uses the API key to
        // look up the state abbreviation. So both of these must have their API keys set whenever the main template's
        // key is changed.

        embeddedOrg.setApiKey( apiKey );
        embeddedPoc.setApiKey( apiKey );
    }

    public List<String> getJustification()
    {
        return justification;
    }

    public void setJustification( List<String> justification )
    {
        this.justification = justification;
    }
}
