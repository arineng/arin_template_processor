package net.arin.tp.processor.template;

import com.google.common.collect.Collections2;
import net.arin.tp.ipaddr.IPVersion;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Base class for net-related templates.
 */
public abstract class NetTemplate extends TemplateImpl implements TemplateWithPublicComments, TemplateWithOriginAses
{
    // NETWORK SECTION
    public static final String NET_NAME_LABEL = "Network Name:";
    private static final String IP_ADDRESS_LABEL = "IP Address and Prefix or Range:";
    public static final String ORIGIN_AS_LABEL = "Origin AS:";

    // OPTIONAL FIELDS
    public static final String PUBLIC_COMMENTS_LABEL = "Public Comments:";
    public static final String ADDITIONAL_INFO_LABEL = "Additional Information:";

    // IGNORED FIELDS
    // We need to include these just so the template parsing is aware of them but they won't be used.
    public static final String NAMESERVER_LABEL = "Hostname of DNS Reverse Mapping Nameserver:";

    // Network variables.
    private String netName;
    private String ipAddress;
    private List<String> originAses = new LinkedList<>();

    // Optional variables.
    private List<String> publicComments = new LinkedList<>();
    private List<String> additionalInfo = new LinkedList<>();

    public abstract IPVersion getIPVersion();

    /**
     * Return the IP Address label. This is a method rather than a constant because it changes for some templates.
     */
    protected String getIpAddressLabel()
    {
        return IP_ADDRESS_LABEL;
    }

    @Override
    protected boolean isMultiLineField( String fieldName )
    {
        return Collections2.transform(
                Arrays.asList( ORIGIN_AS_LABEL, NAMESERVER_LABEL, PUBLIC_COMMENTS_LABEL, ADDITIONAL_INFO_LABEL ),
                normalizeKeyFunction ).contains( fieldName );
    }

    @Override
    protected void mapKeyValuePair( String key, String value )
    {
        if ( normalizedKeyCompare( NET_NAME_LABEL, key ) )
        {
            netName = value;
        }
        else if ( normalizedKeyCompare( getIpAddressLabel(), key ) )
        {
            ipAddress = value;
        }
        else if ( normalizedKeyCompare( ORIGIN_AS_LABEL, key ) )
        {
            originAses.addAll( Arrays.asList( value.split( "\\s*,\\s*" ) ) );
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

    public String getNetName()
    {
        return netName;
    }

    public void setNetName( String netName )
    {
        this.netName = netName;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress( String ipAddress )
    {
        this.ipAddress = ipAddress;
    }

    public List<String> getOriginAses()
    {
        return originAses;
    }

    public void setOriginAses( List<String> originAses )
    {
        this.originAses = originAses;
    }

    public List<String> getPublicComments()
    {
        return publicComments;
    }

    public void setPublicComments( List<String> publicComments )
    {
        this.publicComments = publicComments;
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
