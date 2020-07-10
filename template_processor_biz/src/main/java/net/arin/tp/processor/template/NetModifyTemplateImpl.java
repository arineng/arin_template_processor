package net.arin.tp.processor.template;

import com.google.common.collect.Collections2;
import net.arin.tp.ipaddr.IPVersion;
import net.arin.tp.api.payload.NetBlockPayload;
import net.arin.tp.api.payload.NetPayload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Base class for net modify templates.
 */
public abstract class NetModifyTemplateImpl extends NetTemplate
{
    private static final String IPV4_TEMPLATE_NAME = "ARIN-NET-MOD";
    private static final String IPV6_TEMPLATE_NAME = "ARIN-IPv6-NET-MOD";

    private static final String IPV6_ADDRESS_LABEL = "IPv6 Address and Prefix:";
    private static final String ACTION_LABEL = "Registration Action (M or R):";
    public static final String TECH_POC_HANDLE_LABEL = "Tech POC Handle:";
    public static final String ABUSE_POC_HANDLE_LABEL = "Abuse POC Handle:";
    public static final String NOC_POC_HANDLE_LABEL = "NOC POC Handle:";

    protected Action action;

    private List<String> techPocHandles = new LinkedList<>();
    private List<String> abusePocHandles = new LinkedList<>();
    private List<String> nocPocHandles = new LinkedList<>();

    @Override
    protected String getIpAddressLabel()
    {
        // Both v4 and v5 versions of the IPv6 net modify templates have an oddball IP address label.
        return getIPVersion() == IPVersion.IPV4 ? super.getIpAddressLabel() : IPV6_ADDRESS_LABEL;
    }

    @Override
    protected List<String> getFieldKeyValues()
    {
        List<String> fieldKeyValues = new ArrayList<>();

        fieldKeyValues.addAll( Arrays.asList( ACTION_LABEL, NET_NAME_LABEL, getIpAddressLabel(), ORIGIN_AS_LABEL,
                PUBLIC_COMMENTS_LABEL, NAMESERVER_LABEL, ADDITIONAL_INFO_LABEL, TECH_POC_HANDLE_LABEL,
                ABUSE_POC_HANDLE_LABEL, NOC_POC_HANDLE_LABEL ) );

        return fieldKeyValues;
    }

    @Override
    public Map<String, String> getXmlToFieldKeyMapping()
    {
        Map<String, String> mapping = new HashMap<>();

        mapping.put( NetPayload.FIELD_NETNAME, NET_NAME_LABEL );
        mapping.put( NetPayload.FIELD_NETHANDLE, getIpAddressLabel() );
        mapping.put( NetPayload.FIELD_START_ADDRESS, getIpAddressLabel() );
        mapping.put( NetPayload.FIELD_END_ADDRESS, getIpAddressLabel() );
        mapping.put( NetBlockPayload.FIELD_CIDR_LENGTH, getIpAddressLabel() );
        mapping.put( NetPayload.FIELD_ORIGIN_AS, ORIGIN_AS_LABEL );
        mapping.put( NetPayload.FIELD_POCLINK, "" );
        mapping.put( NetPayload.FIELD_PUBLICCOMMENTS, PUBLIC_COMMENTS_LABEL );

        return mapping;
    }

    @Override
    protected boolean isMultiLineField( String fieldName )
    {
        return super.isMultiLineField( fieldName ) ||
                Collections2.transform(
                        Arrays.asList( TECH_POC_HANDLE_LABEL, ABUSE_POC_HANDLE_LABEL, NOC_POC_HANDLE_LABEL ),
                        normalizeKeyFunction ).contains( fieldName );
    }

    @Override
    public String getTemplateName()
    {
        return getIPVersion() == IPVersion.IPV4 ? IPV4_TEMPLATE_NAME : IPV6_TEMPLATE_NAME;
    }

    public Action getAction()
    {
        return this.action;
    }

    public void setAction( Action action )
    {
        this.action = action;
    }

    public List<String> getTechPocHandles()
    {
        return techPocHandles;
    }

    public void setTechPocHandles( List<String> techPocHandles )
    {
        this.techPocHandles = techPocHandles;
    }

    public List<String> getAbusePocHandles()
    {
        return abusePocHandles;
    }

    public void setAbusePocHandles( List<String> abusePocHandles )
    {
        this.abusePocHandles = abusePocHandles;
    }

    public List<String> getNocPocHandles()
    {
        return nocPocHandles;
    }

    public void setNocPocHandles( List<String> nocPocHandles )
    {
        this.nocPocHandles = nocPocHandles;
    }

    @Override
    protected void mapKeyValuePair( String key, String value )
    {
        // Leverage parent's mapping of core net fields.
        super.mapKeyValuePair( key, value );

        if ( normalizedKeyCompare( ACTION_LABEL, key ) )
        {
            action = Action.fromTemplate( value );
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
    }
}
