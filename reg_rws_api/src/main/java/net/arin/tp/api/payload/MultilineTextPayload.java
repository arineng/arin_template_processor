package net.arin.tp.api.payload;

import org.apache.commons.lang.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a multiline field, like public comments or address.
 */
@XmlRootElement( name = "multiline" )
@XmlAccessorType( XmlAccessType.NONE )
public class MultilineTextPayload implements Payload
{
    private List<Line> lines;

    public MultilineTextPayload()
    {
    }

    public MultilineTextPayload( String string )
    {
        lines = new ArrayList<>();

        if ( string != null )
        {
            String[] lines = string.split( "\\r?\\n" );

            for ( int i = 0; i < lines.length; i++ )
            {
                this.lines.add( new Line( i, lines[i] ) );
            }

        }
    }

    @XmlElements( {
            @XmlElement( name = "line", type = Line.class )
    } )
    public List<Line> getLines()
    {
        return lines;
    }

    public void setLines( List<Line> lines )
    {
        this.lines = lines;
    }

    public String getText()
    {
        List<String> list = new ArrayList<>();

        if ( getLines() == null || getLines().size() == 0 )
        {
            return null;
        }

        for ( Line l : getLines() )
        {
            list.add( l.getText() );
        }

        return StringUtils.join( list, "\n" );
    }

    static public MultilineTextPayload makeMultilineText( String string )
    {
        if ( !StringUtils.isBlank( string ) )
        {
            return new MultilineTextPayload( string );
        }

        return null;
    }

    @Override
    public boolean equals( Object anotherMultiLineText )
    {
        if ( !( anotherMultiLineText instanceof MultilineTextPayload ) )
        {
            return false;
        }

        MultilineTextPayload toCompare = ( MultilineTextPayload ) anotherMultiLineText;

        return ( this.lines == null && toCompare.lines == null ) ||
                ( this.lines != null && this.lines.equals( toCompare.lines ) );
    }

    @Override
    public int hashCode()
    {
        int result = 17;

        result = 37 * result
                + ( this.lines != null ? lines : "" ).hashCode();

        return result;
    }

    @XmlRootElement( name = "line" )
    @XmlAccessorType( XmlAccessType.NONE )
    static public class Line
    {
        private String text = null;
        private Integer number = null;

        public Line()
        {
        }

        public Line( Integer number, String text )
        {
            this.number = number;
            this.text = text;
        }

        @XmlValue
        public String getText()
        {
            return text;
        }

        public void setText( String text )
        {
            this.text = text;
        }

        @XmlAttribute( name = "number" )
        public Integer getNumber()
        {
            return number;
        }

        public void setNumber( Integer number )
        {
            this.number = number;
        }

        @Override
        public boolean equals( Object anotherLine )
        {
            if ( !( anotherLine instanceof Line ) )
            {
                return false;
            }

            Line toCompare = ( Line ) anotherLine;
            return StringUtils.equals( this.text, toCompare.text )
                    && this.number.equals( toCompare.number );
        }

        @Override
        public int hashCode()
        {
            return ( this.number.toString() + text ).hashCode();
        }
    }
}
