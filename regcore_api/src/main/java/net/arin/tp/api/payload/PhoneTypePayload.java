package net.arin.tp.api.payload;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This represents a phone type. It is a nested element of {@link PhonePayload PhonePayload} and should not be
 * submitted by itself. The description element will be inferred from the code element. You do not need to set the
 * description element. If you do, it will be ignored.
 */
@XmlRootElement( name = PhoneTypePayload.FIELD_TYPE )
public class PhoneTypePayload implements Payload
{
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_DESC = "description";

    private Code code;

    /**
     * The <code>code</code> allows you to specify the type of the Phone Number.
     */
    public enum Code
    {
        /**
         * Used to represent an Office phone number.
         */
        O( "OFFICE" ),

        /**
         * Used to represent a Fax phone number.
         */
        F( "FAX" ),

        /**
         * Used to represent a Mobile phone number.
         */
        M( "MOBILE" );

        private String description;

        Code( String description )
        {
            this.description = description;
        }

        public String getDescription()
        {
            return description;
        }
    }

    public PhoneTypePayload()
    {
    }

    public PhoneTypePayload( Code code )
    {
        this.code = code;
    }

    public PhoneTypePayload( String codeStr )
    {
        this.code = Code.valueOf( codeStr );
    }

    @XmlElement( name = FIELD_CODE )
    public Code getCode()
    {
        return code;
    }

    public void setCode( Code code )
    {
        this.code = code;
    }

    @XmlElement( name = FIELD_DESC )
    public String getDescription()
    {
        return code.getDescription();
    }

    public void setDescription( String description )
    {
        // Do nothing. The description field is immutable.
    }

    @Override
    public boolean equals( Object other )
    {
        if ( other == null || !( other instanceof PhoneTypePayload ) )
        {
            return false;
        }

        return code.equals( ( ( PhoneTypePayload ) other ).getCode() );
    }

    @Override
    public int hashCode()
    {
        return code.hashCode();
    }

    @Override
    public String toString()
    {
        return code == null ? null : code.toString();
    }
}
