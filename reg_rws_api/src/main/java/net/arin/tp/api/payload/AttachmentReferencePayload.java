package net.arin.tp.api.payload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>
 * This is a nested element of a {@link MessagePayload MessagePayload} or of a
 * {@link MessageReferencePayload MessageReferencePayload}.
 * </p>
 *
 * <p>
 * This AttachmentReferencePayload should not be submitted by itself.
 * </p>
 */
@XmlRootElement( name = "attachmentReference" )
@XmlAccessorType( XmlAccessType.NONE )
public class AttachmentReferencePayload implements Payload
{
    static final public String FIELD_ATTACHMENT_FILENAME = "attachmentFilename";
    static final public String FIELD_ATTACHMENT_ID = "attachmentId";

    private String attachmentFilename;
    private String attachmentId;

    @XmlElement( name = FIELD_ATTACHMENT_FILENAME )
    public String getAttachmentFilename()
    {
        return attachmentFilename;
    }

    public void setAttachmentFilename( String attachmentFilename )
    {
        this.attachmentFilename = attachmentFilename;
    }

    /**
     * Returns the attachment ID.
     *
     * @return The attachment ID
     */
    @XmlElement( name = FIELD_ATTACHMENT_ID )
    public String getAttachmentId()
    {
        return attachmentId;
    }

    public void setAttachmentId( String attachmentId )
    {
        this.attachmentId = attachmentId;
    }
}
