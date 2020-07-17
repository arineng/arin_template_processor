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

/**
 * <p>
 * The PocPayload structure provides access to the subset of the POC data available via templates or ARIN Online.
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
@XmlRootElement( name = "poc" )
@XmlAccessorType( XmlAccessType.NONE )
public class PocPayload implements PayloadWithPublicComments, PayloadWithCountry
{
    static final public String FIELD_POCHANDLE = "handle";
    static final public String FIELD_CONTACTTYPE = "contactType";
    static final public String FIELD_COMPANYNAME = "companyName";
    static final public String FIELD_FIRSTNAME = "firstName";
    static final public String FIELD_MIDDLENAME = "middleName";
    static final public String FIELD_LASTNAME = "lastName";
    static final public String FIELD_STREET = "streetAddress";
    static final public String FIELD_CITY = "city";
    static final public String FIELD_STATE = "iso3166-2";
    static final public String FIELD_POSTALCODE = "postalCode";
    static final public String FIELD_OFFICEPHONE = "officePhone";
    static final public String FIELD_FAXPHONE = "faxPhone";
    static final public String FIELD_MOBILEPHONE = "mobilePhone";
    static final public String FIELD_EMAIL = "email";
    static final public String FIELD_EMAILS = "emails";
    static final public String FIELD_PHONE = "phone";
    static final public String FIELD_PHONES = "phones";
    static final public String FIELD_REGDATE = "registrationDate";

    private String pocHandle;
    private ContactType contactType;
    private String companyName;
    private String firstName;
    private String middleName;
    private String lastName;
    private MultilineTextPayload street;
    private String city;
    private String state;
    private String postalCode;
    private CountryPayload country;
    private List<PhonePayload> phones;
    private List<String> email;
    private MultilineTextPayload pubComment;
    private Date regDate;

    /**
     * The <code>ContactType</code> allows you to specify if the the contact type is a Person or a Role.
     */
    public enum ContactType
    {
        /**
         * Used to represent a person contact type.
         */
        PERSON,

        /**
         * Used to represent a role contact type.
         */
        ROLE
    }

    @XmlElement( name = FIELD_POCHANDLE )
    @SystemGenerated
    public String getPocHandle()
    {
        return pocHandle;
    }

    public void setPocHandle( String pocHandle )
    {
        this.pocHandle = pocHandle;
    }

    @XmlElement( name = FIELD_CONTACTTYPE )
    @Immutable
    public ContactType getContactType()
    {
        return contactType;
    }

    public void setContactType( ContactType contactType )
    {
        this.contactType = contactType;
    }

    @XmlElement( name = FIELD_COMPANYNAME )
    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName( String companyName )
    {
        this.companyName = companyName;
    }

    @XmlElement( name = FIELD_FIRSTNAME )
    @Immutable
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    @XmlElement( name = FIELD_MIDDLENAME )
    @Immutable
    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName( String middleName )
    {
        this.middleName = middleName;
    }

    @XmlElement( name = FIELD_LASTNAME )
    @Immutable
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( String lastName )
    {
        this.lastName = lastName;
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

    /**
     * Convenience method that does a null check on street. Use this as opposed to say:
     * getMultilineStreet().getMultilineText()
     */
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
    public String getState()
    {
        return state;
    }

    public void setState( String state )
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

    @XmlElement( name = FIELD_EMAIL )
    @XmlElementWrapper( name = FIELD_EMAILS )
    public List<String> getEmail()
    {
        return email;
    }

    public void setEmail( List<String> email )
    {
        this.email = email;
    }

    @XmlElement( name = FIELD_PHONE )
    @XmlElementWrapper( name = FIELD_PHONES )
    public List<PhonePayload> getPhones()
    {
        return phones;
    }

    public void setPhones( List<PhonePayload> phones )
    {
        this.phones = phones;
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

    /**
     * Convenience method that does a null check on pubComment. Use this as opposed to say:
     * getMultilinePubComment().getMultilineText()
     */
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

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        PocPayload that = ( PocPayload ) o;

        if ( pocHandle != null ? !pocHandle.equals( that.pocHandle ) : that.pocHandle != null )
        {
            return false;
        }
        if ( contactType != that.contactType )
        {
            return false;
        }
        if ( companyName != null ? !companyName.equals( that.companyName ) : that.companyName != null )
        {
            return false;
        }
        if ( firstName != null ? !firstName.equals( that.firstName ) : that.firstName != null )
        {
            return false;
        }
        if ( middleName != null ? !middleName.equals( that.middleName ) : that.middleName != null )
        {
            return false;
        }
        if ( lastName != null ? !lastName.equals( that.lastName ) : that.lastName != null )
        {
            return false;
        }
        if ( street != null ? !street.equals( that.street ) : that.street != null )
        {
            return false;
        }
        if ( city != null ? !city.equals( that.city ) : that.city != null )
        {
            return false;
        }
        if ( state != null ? !state.equals( that.state ) : that.state != null )
        {
            return false;
        }
        if ( postalCode != null ? !postalCode.equals( that.postalCode ) : that.postalCode != null )
        {
            return false;
        }
        if ( country != null ? !country.equals( that.country ) : that.country != null )
        {
            return false;
        }
        if ( phones != null ? !phones.equals( that.phones ) : that.phones != null )
        {
            return false;
        }
        if ( email != null ? !email.equals( that.email ) : that.email != null )
        {
            return false;
        }
        if ( pubComment != null ? !pubComment.equals( that.pubComment ) : that.pubComment != null )
        {
            return false;
        }
        return regDate != null ? regDate.equals( that.regDate ) : that.regDate == null;
    }

    @Override
    public int hashCode()
    {
        int result = pocHandle != null ? pocHandle.hashCode() : 0;
        result = 31 * result + ( contactType != null ? contactType.hashCode() : 0 );
        result = 31 * result + ( companyName != null ? companyName.hashCode() : 0 );
        result = 31 * result + ( firstName != null ? firstName.hashCode() : 0 );
        result = 31 * result + ( middleName != null ? middleName.hashCode() : 0 );
        result = 31 * result + ( lastName != null ? lastName.hashCode() : 0 );
        result = 31 * result + ( street != null ? street.hashCode() : 0 );
        result = 31 * result + ( city != null ? city.hashCode() : 0 );
        result = 31 * result + ( state != null ? state.hashCode() : 0 );
        result = 31 * result + ( postalCode != null ? postalCode.hashCode() : 0 );
        result = 31 * result + ( country != null ? country.hashCode() : 0 );
        result = 31 * result + ( phones != null ? phones.hashCode() : 0 );
        result = 31 * result + ( email != null ? email.hashCode() : 0 );
        result = 31 * result + ( pubComment != null ? pubComment.hashCode() : 0 );
        result = 31 * result + ( regDate != null ? regDate.hashCode() : 0 );
        return result;
    }
}
