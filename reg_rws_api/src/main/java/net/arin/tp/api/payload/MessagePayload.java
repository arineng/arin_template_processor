package net.arin.tp.api.payload;

import net.arin.tp.api.annotations.SystemGenerated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * This payload was created to support the sending of additional information to an existing ticket and to enable users
 * to get a specific message and any accompanying attachment(s).
 * </p>
 *
 * <p>
 * It can also be in a {@link NetPayload NetPayload} or an {@link OrgPayload OrgPayload}.
 * </p>
 *
 * <p>
 * When performing the 'Get Message' call as specified in the Methods documentation, the following payload will be
 * returned.
 * </p>
 *
 * <p>
 * <h3>PAYLOAD WHEN GETTING A SPECIFIC MESSAGE</h3>
 * <pre>
 *  &lt;message xmlns=&quot;http://www.arin.net/regrws/core/v1&quot; xmlns:ns2=&quot;http://www.arin.net/regrws/messages/v1&quot;&gt;
 * 	&lt;ns2:messageId&gt;MESSAGEID&lt;/ns2:messageId&gt;
 * 	&lt;ns2:createdDate&gt;Tue Feb 28 17:41:17 EST 2012&lt;/ns2:createdDate&gt;
 * 	&lt;subject&gt;&lt;/subject&gt;
 * 	&lt;text&gt;
 * 		&lt;line number=&quot;&quot;&gt;&lt;/line&gt;
 * 	&lt;/text&gt;
 * 	&lt;category&gt;&lt;/category&gt;
 * 	&lt;attachmentReferences&gt;
 * 		&lt;attachmentReference&gt;
 * 			&lt;attachmentFilename&gt;&lt;/attachmentFilename&gt;
 * 			&lt;attachmentId&gt;&lt;/attachmentId&gt;
 * 		&lt;/attachmentReference&gt;
 * 	&lt;/attachmentReferences&gt;
 * &lt;/message&gt;
 * </pre>
 * </p>
 *
 * <p>
 * <h3>EXAMPLE PAYLOAD WHEN GETTING A SPECIFIC MESSAGE</h3>
 * <pre>
 * &lt;message xmlns=&quot;http://www.arin.net/regrws/core/v1&quot; xmlns:ns2=&quot;http://www.arin.net/regrws/messages/v1&quot;&gt;
 * 	&lt;ns2:messageId&gt;MESSAGEID&lt;ns2:messageId&gt;
 * 	&lt;ns2:createdDate&gt;Tue Feb 28 17:41:17 EST 2012&lt;ns2:createdDate&gt;
 * 	&lt;subject&gt;SUBJECT&lt;/subject&gt;
 * 	&lt;text&gt;
 * 		&lt;line number = &quot;1&quot;&gt;Line 1&lt;/line&gt;
 * 	&lt;/text&gt;
 * 	&lt;category&gt;NONE&lt;/category&gt;
 * 	&lt;attachmentReferences&gt;
 * 		&lt;attachmentReference&gt;
 * 			&lt;attachementFilename&gt;ATTACHMENTFILENAME&lt;/attachmentFilename&gt;
 * 			&lt;attachmentId&gt;ATTACHMENTID&lt;/attachmentId&gt;
 * 		&lt;/attachmentReference&gt;
 * 	&lt;/attachmentReferences&gt;
 * &lt;/message&gt;
 * </pre>
 * </p>
 *
 * <p>
 * These fields will be set by our registration system and may not be set or modified by you. Do not include these
 * fields when adding a Message, or an error will result.
 * </p>
 *
 * <p>
 * The body of the payload will vary depending on the action requested.
 * </p>
 *
 * <p>
 * When performing the 'Add Message' call as specified in the Methods documentation, use the following payload.
 * </p>
 */
@XmlRootElement( name = "message" )
@XmlAccessorType( XmlAccessType.NONE )
public class MessagePayload implements Payload
{
    static final public String FIELD_TEXT = "text";
    static final public String FIELD_SUBJECT = "subject";
    static final public String FIELD_CATEGORY = "category";
    static final public String FIELD_MESSAGEID = "messageId";
    static final public String FIELD_CREATEDDATE = "createdDate";
    static final public String FIELD_ATTACHMENT = "attachment";
    static final public String FIELD_ATTACHMENTS = "attachments";
    static final public String FIELD_ATTACHMENT_REFERENCE = "attachmentReference";
    static final public String FIELD_ATTACHMENT_REFERENCES = "attachmentReferences";

    static final public String NAMESPACE2 = "http://www.arin.net/regrws/messages/v1";

    /**
     * The <code>Category</code> allows you to specify category of the message.
     */
    public enum Category
    {
        /**
         * Used if there is no category type.
         */
        NONE,

        /**
         * Used to represent justification message type.
         */
        JUSTIFICATION
    }

    private String subject;
    private MultilineTextPayload text;
    private List<AttachmentPayload> attachments;
    private List<AttachmentReferencePayload> attachmentReferences;
    private Category category = Category.NONE;
    private Integer messageId;
    private Date createdDate;

    @XmlElement( name = FIELD_SUBJECT )
    public String getSubject()
    {
        return subject;
    }

    public void setSubject( String subject )
    {
        this.subject = subject;
    }

    @XmlElement( name = FIELD_TEXT )
    public MultilineTextPayload getMultilineText()
    {
        return text;
    }

    public void setMultilineText( MultilineTextPayload text )
    {
        this.text = text;
    }

    public String getText()
    {
        if ( text == null )
        {
            return null;
        }

        return text.getText();
    }

    public void setText( String text )
    {
        this.text = MultilineTextPayload.makeMultilineText( text );
    }

    @XmlElement( name = FIELD_CATEGORY )
    public Category getCategory()
    {
        return category;
    }

    public void setCategory( Category category )
    {
        this.category = category;
    }

    @XmlElementWrapper( name = FIELD_ATTACHMENTS )
    @XmlElement( name = FIELD_ATTACHMENT )
    public List<AttachmentPayload> getAttachments()
    {
        return attachments;
    }

    public void setAttachments( List<AttachmentPayload> attachments )
    {
        this.attachments = attachments;
    }

    public void addAttachment( AttachmentPayload attachment )
    {
        if ( attachments == null )
        {
            attachments = new ArrayList<>();
        }

        attachments.add( attachment );
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

    @XmlElement( name = FIELD_MESSAGEID, namespace = NAMESPACE2 )
    @SystemGenerated
    public Integer getMessageId()
    {
        return messageId;
    }

    public void setMessageId( Integer messageId )
    {
        this.messageId = messageId;
    }

    @XmlElement( name = FIELD_CREATEDDATE, namespace = NAMESPACE2 )
    @SystemGenerated
    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate( Date createdDate )
    {
        this.createdDate = createdDate;
    }

    /**
     * This is a utility method for creating an "Additional Information" message. Simply give it the text and it will
     * handle the creation of the rest of the payload for you.
     *
     * @param text The "additional information"
     * @return A MessagePayload object to send along
     */
    static public MessagePayload createAdditionalInformation( String text )
    {
        MessagePayload payload = new MessagePayload();

        payload.setText( text );

        return payload;
    }
}
