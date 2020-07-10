package net.arin.tp.processor.template;

import com.google.common.collect.Collections2;
import net.arin.tp.api.payload.CustomerPayload;
import net.arin.tp.api.payload.NetBlockPayload;
import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.service.Parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public abstract class ReassignSimpleTemplateImpl extends TemplateImpl implements TemplateWithPublicComments, TemplateWithOriginAses
{
    private static final String TEMPLATE_NAME = "ARIN-REASSIGN-SIMPLE";

    private static final String ACTION_LABEL = "Registration Action (N,M, or R):";
    private static final String NET_NAME_LABEL = "Network Name:";
    private static final String IP_ADDRESS_LABEL = "IP Address and Prefix or Range:";
    private static final String ORIGIN_AS_LABEL = "Origin AS:";
    private static final String PRIVATE_LABEL = "Private (Yes or No):";
    private static final String CUSTOMER_NAME_LABEL = "Customer Name:";
    private static final String CUSTOMER_ADDRESS_LABEL = "Customer Address:";
    private static final String CUSTOMER_CITY_LABEL = "Customer City:";
    private static final String CUSTOMER_STATE_LABEL = "Customer State/Province:";
    private static final String CUSTOMER_POSTAL_CODE_LABEL = "Customer Postal Code:";
    private static final String CUSTOMER_COUNTRY_CODE_LABEL = "Customer Country Code:";
    private static final String PUBLIC_COMMENTS_LABEL = "Public Comments:";

    protected Action action;
    protected String netName;
    protected String ipAddress;
    protected List<String> originAses = new LinkedList<>();
    protected String privateCustomer;
    protected String customerName;
    protected List<String> address = new LinkedList<>();
    protected String city;
    protected String stateProvince;
    protected String postalCode;
    protected String countryCode;
    protected List<String> pubComment = new LinkedList<>();

    @Override
    public Map<String, String> getXmlToFieldKeyMapping()
    {
        Map<String, String> mapping = new HashMap<>();

        mapping.put( NetPayload.FIELD_NETNAME, NET_NAME_LABEL );
        mapping.put( NetPayload.FIELD_START_ADDRESS, IP_ADDRESS_LABEL );
        mapping.put( NetPayload.FIELD_END_ADDRESS, IP_ADDRESS_LABEL );
        mapping.put( NetPayload.FIELD_NETHANDLE, IP_ADDRESS_LABEL );
        mapping.put( Parameters.PARENT_NET_HANDLE, IP_ADDRESS_LABEL );
        mapping.put( NetBlockPayload.FIELD_CIDR_LENGTH, IP_ADDRESS_LABEL );
        mapping.put( NetPayload.FIELD_ORIGIN_AS, ORIGIN_AS_LABEL );
        mapping.put( NetPayload.FIELD_PUBLICCOMMENTS, PUBLIC_COMMENTS_LABEL );

        mapping.put( CustomerPayload.FIELD_NAME, CUSTOMER_NAME_LABEL );
        mapping.put( CustomerPayload.FIELD_STREET, CUSTOMER_ADDRESS_LABEL );
        mapping.put( CustomerPayload.FIELD_CITY, CUSTOMER_CITY_LABEL );
        mapping.put( CustomerPayload.FIELD_STATE, CUSTOMER_STATE_LABEL );
        mapping.put( CustomerPayload.FIELD_POSTALCODE, CUSTOMER_POSTAL_CODE_LABEL );
        mapping.put( CustomerPayload.FIELD_COUNTRY, CUSTOMER_COUNTRY_CODE_LABEL );
        mapping.put( CustomerPayload.FIELD_PUBLICCOMMENTS, PUBLIC_COMMENTS_LABEL );
        mapping.put( CustomerPayload.FIELD_PRIVATECUSTOMER, PRIVATE_LABEL );

        return mapping;
    }

    @Override
    protected List<String> getFieldKeyValues()
    {
        return Arrays.asList( ACTION_LABEL, NET_NAME_LABEL, IP_ADDRESS_LABEL, ORIGIN_AS_LABEL, PRIVATE_LABEL, CUSTOMER_NAME_LABEL,
                CUSTOMER_ADDRESS_LABEL, CUSTOMER_CITY_LABEL, CUSTOMER_STATE_LABEL, CUSTOMER_POSTAL_CODE_LABEL, CUSTOMER_COUNTRY_CODE_LABEL,
                PUBLIC_COMMENTS_LABEL );
    }

    @Override
    protected boolean isMultiLineField( String fieldName )
    {
        if ( Collections2.transform(
                Arrays.asList(
                        ACTION_LABEL, NET_NAME_LABEL, IP_ADDRESS_LABEL, PRIVATE_LABEL, CUSTOMER_NAME_LABEL,
                        CUSTOMER_CITY_LABEL, CUSTOMER_STATE_LABEL, CUSTOMER_POSTAL_CODE_LABEL,
                        CUSTOMER_COUNTRY_CODE_LABEL ), normalizeKeyFunction ).contains( fieldName ) )
        {
            return false;
        }
        else
        {
            return true;
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

    public String getPrivateCustomer()
    {
        return privateCustomer;
    }

    public void setPrivateCustomer( String privateCustomer )
    {
        this.privateCustomer = privateCustomer;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName( String customerName )
    {
        this.customerName = customerName;
    }

    public List<String> getAddress()
    {
        return address;
    }

    public void setAddress( List<String> address )
    {
        this.address = address;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity( String city )
    {
        this.city = city;
    }

    public String getStateProvince()
    {
        return stateProvince;
    }

    public void setStateProvince( String stateProvince )
    {
        this.stateProvince = stateProvince;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode( String postalCode )
    {
        this.postalCode = postalCode;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode( String countryCode )
    {
        this.countryCode = countryCode;
    }

    public List<String> getPublicComments()
    {
        return pubComment;
    }

    public void setPublicComments( List<String> pubComment )
    {
        this.pubComment = pubComment;
    }

    @Override
    protected void mapKeyValuePair( String key, String value )
    {
        if ( normalizedKeyCompare( ACTION_LABEL, key ) )
        {
            action = Action.fromTemplate( value );
        }
        else if ( normalizedKeyCompare( NET_NAME_LABEL, key ) )
        {
            netName = value;
        }
        else if ( normalizedKeyCompare( IP_ADDRESS_LABEL, key ) )
        {
            ipAddress = value;
        }
        else if ( normalizedKeyCompare( ORIGIN_AS_LABEL, key ) )
        {
            StringTokenizer st = new StringTokenizer( value, "," );
            while ( st.hasMoreTokens() )
            {
                String originAs = st.nextToken().trim().toUpperCase();
                originAses.add( originAs );
            }
        }
        else if ( normalizedKeyCompare( PRIVATE_LABEL, key ) )
        {
            if ( value.equalsIgnoreCase( TemplateImpl.YES ) )
            {
                privateCustomer = "Y";
            }
            else if ( value.equalsIgnoreCase( TemplateImpl.NO ) )
            {
                privateCustomer = "N";
            }
            else
            {
                privateCustomer = value.toUpperCase();
            }
        }
        else if ( normalizedKeyCompare( CUSTOMER_NAME_LABEL, key ) )
        {
            customerName = value;
        }
        else if ( normalizedKeyCompare( CUSTOMER_ADDRESS_LABEL, key ) )
        {
            address.add( value );
        }
        else if ( normalizedKeyCompare( CUSTOMER_CITY_LABEL, key ) )
        {
            city = value;
        }
        else if ( normalizedKeyCompare( CUSTOMER_STATE_LABEL, key ) )
        {
            stateProvince = value;
        }
        else if ( normalizedKeyCompare( CUSTOMER_POSTAL_CODE_LABEL, key ) )
        {
            postalCode = value;
        }
        else if ( normalizedKeyCompare( CUSTOMER_COUNTRY_CODE_LABEL, key ) )
        {
            countryCode = value;
        }
        else if ( normalizedKeyCompare( PUBLIC_COMMENTS_LABEL, key ) )
        {
            pubComment.add( value );
        }
    }

    @Override
    public String getTemplateName()
    {
        return TEMPLATE_NAME;
    }
}
