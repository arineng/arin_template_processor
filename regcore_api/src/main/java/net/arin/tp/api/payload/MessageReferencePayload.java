package net.arin.tp.api.payload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This is a nested element of a {@link TicketPayload TicketPayload} returned when a GetTicketDetails call is performed
 * and the msgRefs parameter is specified as 'true'. You can then request a GetMessage call with a specified MessageID,
 * and will be returned a {@link MessagePayload MessagePayload} as specified above.
 * </p>
 *
 * <p>
 * It can also be in a {@link NetPayload NetPayload} or an {@link OrgPayload OrgPayload}.
 * </p>
 *
 * <p>
 * This MessageReferencePayload should not be submitted by itself.
 * </p>
 */
@XmlRootElement( name = "messageReference" )
@XmlAccessorType( XmlAccessType.NONE )
public class MessageReferencePayload implements Payload
{
    static final public String FIELD_MESSAGE_ID = "messageId";

    static final public String FIELD_ATTACHMENT_REFERENCE = "attachmentReference";
    static final public String FIELD_ATTACHMENT_REFERENCES = "attachmentReferences";

    private List<AttachmentReferencePayload> attachmentReferences;

    private int messageId;

    @XmlElement( name = FIELD_MESSAGE_ID )
    public int getMessageId()
    {
        return messageId;
    }

    public void setMessageId( int messageId )
    {
        this.messageId = messageId;
    }

    @XmlElementWrapper( name = FIELD_ATTACHMENT_REFERENCES )
    @XmlElement( name = FIELD_ATTACHMENT_REFERENCE )
    public List<AttachmentReferencePayload> getAttachmentReferences()
    {
        return attachmentReferences;
    }

    public void setAttachmentReferences( List<AttachmentReferencePayload> attachmentReferences )
    {
        this.attachmentReferences = attachmentReferences;
    }

    public void addAttachmentReference( AttachmentReferencePayload attachmentReference )
    {
        if ( attachmentReferences == null )
        {
            attachmentReferences = new ArrayList<>();
        }

        attachmentReferences.add( attachmentReference );
    }
}
