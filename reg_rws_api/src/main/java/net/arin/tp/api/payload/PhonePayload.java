package net.arin.tp.api.payload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * The PhonePayload structure provides access to the phone related data available via templates or ARIN Online. The
 * <code>PhonePayload</code> is used by the {@link PocPayload PocPayload} and as a standalone structure by the
 * PocService method {@link net.arin.tp.api.service.PocService#addPhone(String, String, PhonePayload)}.
 * </p>
 *
 * <p>
 * The <code>number</code> field should be in NANP format if applicable. The <code>extension</code> is optional and can
 * be left blank or not included in the payload.
 * </p>
 */
@XmlRootElement( name = "phone" )
@XmlAccessorType( XmlAccessType.NONE )
public class PhonePayload implements Payload
{
    static final public String FIELD_TYPE = "type";
    static final public String FIELD_NUMBER = "number";
    static final public String FIELD_EXTENSION = "extension";

    private PhoneTypePayload type;
    private String number;
    private String extension;

    @XmlElement( name = FIELD_EXTENSION )
    public String getExtension()
    {
        return extension;
    }

    public void setExtension( String extension )
    {
        this.extension = extension;
    }

    @XmlElement( name = FIELD_TYPE )
    public PhoneTypePayload getType()
    {
        return type;
    }

    public void setType( PhoneTypePayload type )
    {
        this.type = type;
    }

    public void setType( PhoneTypePayload.Code code )
    {
        this.type = new PhoneTypePayload( code );
    }

    @XmlElement( name = FIELD_NUMBER )
    public String getNumber()
    {
        return number;
    }

    public void setNumber( String number )
    {
        this.number = number;
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

        PhonePayload that = ( PhonePayload ) o;

        if ( type != null ? !type.equals( that.type ) : that.type != null )
        {
            return false;
        }
        if ( number != null ? !number.equals( that.number ) : that.number != null )
        {
            return false;
        }
        return extension != null ? extension.equals( that.extension ) : that.extension == null;
    }

    @Override
    public int hashCode()
    {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + ( number != null ? number.hashCode() : 0 );
        result = 31 * result + ( extension != null ? extension.hashCode() : 0 );
        return result;
    }
}
