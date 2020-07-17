package net.arin.tp.api.payload;

import net.arin.tp.api.annotations.Immutable;
import net.arin.tp.api.annotations.SystemGenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * The OrgPayload structure provides access to the subset of the Organization data available via templates or ARIN
 * Online.
 * </p>
 *
 * <p>
 * Note: The element name <strong>iso3166-2</strong> represents the ISO standard for the state/province code for the
 * ISO3166-1 countries.
 * </p>
 *
 * <p>
 * The element name orgURL is meant for a Referral Whois (RWhois) server hostname and port, <b>not</b> for the URL of
 * the company's website. RWhois is a protocol typically run on port 4321 and is described in RFC 2167.
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
@XmlRootElement( name = "org" )
@XmlAccessorType( XmlAccessType.NONE )
public class OrgPayload extends PocLinkablePayload implements PayloadWithPublicComments, PayloadWithCountry
{
    static final public String FIELD_ORGHANDLE = "handle";
    static final public String FIELD_ORGNAME = "orgName";
    static final public String FIELD_DBANAME = "dbaName";
    static final public String FIELD_TAXID = "taxId";
    static final public String FIELD_STREET = "streetAddress";
    static final public String FIELD_CITY = "city";
    static final public String FIELD_STATE = "iso3166-2";
    static final public String FIELD_POSTALCODE = "postalCode";
    static final public String FIELD_ORGURL = "orgUrl";
    static final public String FIELD_MESSAGE = "message";
    static final public String FIELD_MESSAGES = "messages";
    static final public String FIELD_MESSAGE_REFERENCE = "messageReference";
    static final public String FIELD_MESSAGE_REFERENCES = "messageReferences";

    private String orgHandle;

    // Org info.
    private String orgName;
    private String dbaName;
    private String taxId;

    // Address.
    private MultilineTextPayload street;
    private String city;
    private String stateProvince;
    private String postalCode;
    private CountryPayload country;

    // Other.
    private String orgUrl;
    private MultilineTextPayload pubComment;

    private Date regDate;

    private List<MessagePayload> messages;
    private List<MessageReferencePayload> messageReferences;

    @XmlElement( name = FIELD_ORGHANDLE )
    @SystemGenerated
    public String getOrgHandle()
    {
        return orgHandle;
    }

    public void setOrgHandle( String orgHandle )
    {
        this.orgHandle = orgHandle;
    }

    @XmlElement( name = FIELD_ORGNAME )
    @Immutable
    public String getOrgName()
    {
        return orgName;
    }

    public void setOrgName( String orgName )
    {
        this.orgName = orgName;
    }

    @XmlElement( name = FIELD_DBANAME )
    @Immutable
    public String getDbaName()
    {
        return dbaName;
    }

    public void setDbaName( String dbaName )
    {
        this.dbaName = dbaName;
    }

    @XmlElement( name = FIELD_TAXID )
    public String getTaxId()
    {
        return taxId;
    }

    public void setTaxId( String taxId )
    {
        this.taxId = taxId;
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
        setMultilineStreet( MultilineTextPayload.makeMultilineText( street ) );
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
        return stateProvince;
    }

    public void setStateProvince( String stateProvince )
    {
        this.stateProvince = stateProvince;
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

    @XmlElement( name = FIELD_ORGURL )
    public String getOrgUrl()
    {
        return orgUrl;
    }

    public void setOrgUrl( String orgUrl )
    {
        this.orgUrl = orgUrl;
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
        setMultilinePublicComments( MultilineTextPayload.makeMultilineText( pubComment ) );
    }

    @XmlElement( name = "registrationDate" )
    @SystemGenerated
    public Date getRegDate()
    {
        return regDate;
    }

    public void setRegDate( Date regDate )
    {
        this.regDate = regDate;
    }

    public String getAdminPoc()
    {
        Set<String> admins = getPocHandlesForFunction( PocLinkPayload.Function.AD );

        // There should only be ONE admin POC if there are any at all.
        return ( admins != null && admins.size() > 0 ) ? admins.iterator().next() : null;
    }

    /**
     * Set the Admin POC (remove the current one if it exists).
     */
    public void setAdminPoc( String pocHandle )
    {
        clearAdminPoc();
        getPocLinks().add( new PocLinkPayload( pocHandle, PocLinkPayload.Function.AD ) );
    }

    public void clearAdminPoc()
    {
        clearPocLinksForFunction( PocLinkPayload.Function.AD );
    }

    @XmlElement( name = FIELD_MESSAGE )
    @XmlElementWrapper( name = FIELD_MESSAGES )
    public List<MessagePayload> getMessages()
    {
        return messages;
    }

    public void setMessages( List<MessagePayload> messages )
    {
        this.messages = messages;
    }

    @XmlElement( name = FIELD_MESSAGE_REFERENCE )
    @XmlElementWrapper( name = FIELD_MESSAGE_REFERENCES )
    public List<MessageReferencePayload> getMessageReferences()
    {
        return messageReferences;
    }

    public void setMessageReferences( List<MessageReferencePayload> messageReferences )
    {
        this.messageReferences = messageReferences;
    }
}
