package net.arin.tp.api.payload;

import net.arin.tp.api.annotations.SystemGenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * <p>
 * The CustomerPayload structure provides access to the subset of the Customer data available via templates or ARIN
 * Online.
 * </p>
 *
 * <p>
 * The main difference between the <code>OrgPayload</code> and <code>CustomerPayload</code> is the
 * <code>privateCustomer</code> attribute. If <code>privateCustomer</code> is set to be <code>true</code> then whenever
 * the record is displayed, as associated with a network or other resource, it will appear as "Private Customer." The
 * name and address fields will not be visible to the general public. If the <code>privateCustomer</code> is set to be
 * <code>false</code> then the Customer will be visible as if it were any other Organization record. If no value is
 * specified, your customer will not be private.
 * </p>
 *
 * <p>
 * Additionally, the <code>CustomerPayload</code> does not have a <code>dbaName</code>, <code>taxId</code>,
 * <code>orgUrl</code>, or any related POCs.
 * </p>
 *
 * <p>
 * Note: The element name <strong>iso3166-2</strong> represents the ISO standard for the state/province code for the
 * ISO3166-1 countries.
 * </p>
 *
 * <p>
 * The comment element can be used to display information on an object in ARIN's Whois. All comments must be operational
 * in nature. All comments must be accurate. ARIN reserves the right to edit or remove public comments.
 * </p>
 *
 * <p>
 * Suggested comments are:
 * <ul style='list-style:none'>
 * <li>http://www.example.net</li>
 * <li>Standard NOC hours are 7am to 11pm EST</li>
 * </ul>
 * </p>
 */
@XmlRootElement( name = "customer" )
@XmlAccessorType( XmlAccessType.NONE )
public class CustomerPayload implements PayloadWithPublicComments, PayloadWithCountry
{
    static final public String FIELD_HANDLE = "handle";
    static final public String FIELD_NAME = "customerName";
    static final public String FIELD_STREET = "streetAddress";
    static final public String FIELD_CITY = "city";
    static final public String FIELD_STATE = "iso3166-2";
    static final public String FIELD_POSTALCODE = "postalCode";
    static final public String FIELD_PARENTORGHANDLE = "parentOrgHandle";
    static final public String FIELD_REGDATE = "registrationDate";
    static final public String FIELD_PRIVATECUSTOMER = "privateCustomer";

    private String handle;
    private String name;

    private MultilineTextPayload street;
    private String city;
    private String state;
    private String postalCode;
    private CountryPayload country;

    private MultilineTextPayload pubComment;
    private String parentOrgHandle;
    private Date regDate;
    private Boolean privateCustomer;

    @XmlElement( name = FIELD_HANDLE )
    @SystemGenerated
    public String getHandle()
    {
        return handle;
    }

    public void setHandle( String handle )
    {
        this.handle = handle;
    }

    @XmlElement( name = FIELD_NAME )
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @XmlElement( name = FIELD_STREET )
    public MultilineTextPayload getMultilineStreet()
    {
        return street;
    }

    public void setMultilineStreet( MultilineTextPayload street )
    {
        this.street = street;
    }

    public String getStreet()
    {
        if ( street == null )
        {
            return null;
        }

        return street.getText();
    }

    public void setStreet( String street )
    {
        this.street = MultilineTextPayload.makeMultilineText( street );
    }

    @XmlElement( name = FIELD_CITY )
    public String getCity()
    {
        return city;
    }

    public void setCity( String city )
    {
        this.city = city;
    }

    @XmlElement( name = FIELD_STATE )
    public String getStateProvince()
    {
        return state;
    }

    public void setStateProvince( String state )
    {
        this.state = state;
    }

    @XmlElement( name = FIELD_POSTALCODE )
    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode( String postalCode )
    {
        this.postalCode = postalCode;
    }

    @XmlElement( name = FIELD_COUNTRY )
    public CountryPayload getCountry()
    {
        return country;
    }

    public void setCountry( CountryPayload country )
    {
        this.country = country;
    }

    @XmlElement( name = FIELD_PUBLICCOMMENTS )
    public MultilineTextPayload getMultilinePublicComments()
    {
        return pubComment;
    }

    public void setMultilinePublicComments( MultilineTextPayload pubComment )
    {
        this.pubComment = pubComment;
    }

    public String getPublicComments()
    {
        if ( pubComment == null )
        {
            return null;
        }

        return pubComment.getText();
    }

    public void setPublicComments( String pubComment )
    {
        this.pubComment = MultilineTextPayload.makeMultilineText( pubComment );
    }

    @XmlElement( name = FIELD_PARENTORGHANDLE )
    public String getParentOrgHandle()
    {
        return parentOrgHandle;
    }

    public void setParentOrgHandle( String parentOrgHandle )
    {
        this.parentOrgHandle = parentOrgHandle;
    }

    @XmlElement( name = FIELD_REGDATE )
    @SystemGenerated
    public Date getRegDate()
    {
        return regDate;
    }

    public void setRegDate( Date regDate )
    {
        this.regDate = regDate;
    }

    @XmlElement( name = FIELD_PRIVATECUSTOMER )
    public Boolean isPrivateCustomer()
    {
        return privateCustomer;
    }

    public void setPrivateCustomer( Boolean privateCustomer )
    {
        this.privateCustomer = privateCustomer;
    }
}
