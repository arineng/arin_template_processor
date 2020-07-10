package net.arin.tp.api.payload;

import org.apache.commons.codec.binary.Base64;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * This is a nested element of a {@link MessagePayload MessagePayload}. The AttachmentPayload structure provides a way
 * to add attachments.
 * </p>
 *
 * <p>
 * This AttachmentPayload should not be submitted by itself.
 * </p>
 */
@XmlRootElement( name = "attachment" )
@XmlAccessorType( XmlAccessType.NONE )
public class AttachmentPayload implements Payload
{
    static final public String FIELD_FILENAME = "filename";
    static final public String FIELD_DATA = "data";

    private String filename;
    private String data;

    @XmlElement( name = FIELD_FILENAME )
    public String getFilename()
    {
        return filename;
    }

    public void setFilename( String filename )
    {
        this.filename = filename;
    }

    /**
     * Returns the data from this attachment as a base64 encoded string.
     *
     * @return A base64 encoded representation of the data
     */
    @XmlElement( name = FIELD_DATA )
    public String getData()
    {
        return data;
    }

    /**
     * Returns the data from this attachment as a byte array.
     */
    public byte[] getDataAsByteArray()
    {
        if ( data == null )
        {
            return null;
        }
        else
        {
            return Base64.decodeBase64( data.getBytes() );
        }
    }

    /**
     * Sets the data. This MUST be a base64 encoded string.
     *
     * @param data A base64 encoded representation of the data
     */
    public void setData( String data )
    {
        this.data = data;
    }

    /**
     * Takes a byte array, applies Base64 encoding, and stores the data.
     *
     * @param data Raw binary data to be base64 encoded
     */
    public void setData( byte[] data )
    {
        this.data = new String( Base64.encodeBase64( data ) );
    }
}
