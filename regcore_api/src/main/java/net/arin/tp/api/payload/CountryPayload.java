package net.arin.tp.api.payload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * The CountryPayload structure represents a country using the ISO 3166-1 standard for country codes.
 * </p>
 *
 * <p>
 * The <strong>name</strong> and <strong>e164</strong> elements are not required. You must specify either the 2-code or
 * 3-code elements. If you specify both then they must match.
 * </p>
 *
 * <p>
 * Note: The element name <strong>e164</strong> represents the ITU-T E.164 international calling code.
 * </p>
 */
@XmlRootElement( name = "iso3166-1" )
@XmlAccessorType( XmlAccessType.NONE )
public class CountryPayload implements Payload
{
    private String name;
    private String alpha2Code;
    private String alpha3Code;
    private Integer e164Code;

    public CountryPayload()
    {
    }

    public CountryPayload( String alpha2Code )
    {
        setAlpha2Code( alpha2Code );
    }

    @XmlElement( name = "name" )
    public String getName()
    {
        return name;
    }

    @XmlElement( name = "code2" )
    public String getAlpha2Code()
    {
        return alpha2Code;
    }

    @XmlElement( name = "code3" )
    public String getAlpha3Code()
    {
        return alpha3Code;
    }

    @XmlElement( name = "e164" )
    public Integer getE164Code()
    {
        return e164Code;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public void setAlpha2Code( String alpha2Code )
    {
        this.alpha2Code = alpha2Code;
    }

    public void setAlpha3Code( String alpha3Code )
    {
        this.alpha3Code = alpha3Code;
    }

    public void setE164Code( Integer e164Code )
    {
        this.e164Code = e164Code;
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

        CountryPayload that = ( CountryPayload ) o;

        if ( name != null ? !name.equals( that.name ) : that.name != null )
        {
            return false;
        }
        if ( alpha2Code != null ? !alpha2Code.equals( that.alpha2Code ) : that.alpha2Code != null )
        {
            return false;
        }
        if ( alpha3Code != null ? !alpha3Code.equals( that.alpha3Code ) : that.alpha3Code != null )
        {
            return false;
        }
        return e164Code != null ? e164Code.equals( that.e164Code ) : that.e164Code == null;
    }

    @Override
    public int hashCode()
    {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + ( alpha2Code != null ? alpha2Code.hashCode() : 0 );
        result = 31 * result + ( alpha3Code != null ? alpha3Code.hashCode() : 0 );
        result = 31 * result + ( e164Code != null ? e164Code.hashCode() : 0 );
        return result;
    }
}
