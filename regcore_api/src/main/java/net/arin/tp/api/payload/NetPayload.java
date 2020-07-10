package net.arin.tp.api.payload;

import net.arin.tp.api.annotations.Immutable;
import net.arin.tp.api.annotations.SystemGenerated;
import net.arin.tp.api.utils.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

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
 * The NetPayload structure provides access to the subset of the Network data available via templates or ARIN Online.
 * </p>
 *
 * <p>
 * In cases where this payload is being sent to the service, and <code>netBlock</code> is required; you will only need
 * to populate the <code>endAddress</code> or the <code>cidrLength</code> but not both. The service will calculate the
 * other for you. You will receive both the <code>endAddress</code> and <code>cidrLength</code> elements when the
 * payload is a return value from a service.
 * </p>
 *
 * <p>
 * If you specify a <code>type</code> it must be a valid NetType and it must be the correct type for the action you are
 * trying to perform (reassign or reallocate). If you do not provide a type, it will be determined for you, depending on
 * which service you are calling.
 * </p>
 *
 * <p>
 * The version element may have a value of "4" or "6". If you specify the version, then it must align with the version
 * of the netBlock's <code>startAddress</code>. If you do not provide the version value, then it will be determined for
 * you based on the <code>startAddress</code>.
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
 *
 * <p>
 * The <code>orgHandle</code> and <code>customerHandle</code> elements are mutually exclusive. Depending on the type of
 * the call this payload is being used for, you will be required to assign either a customer or an organization. Either
 * the <code>orgHandle</code> or the <code>customerHandle</code> will be present at all times. Simple reassignments will
 * rely on the customer; detailed reassignments and reallocations require an organization. See the page on
 * {@link net.arin.tp.api.service.NetService#reassign(String, String, NetPayload) reassign} for more information.
 * </p>
 */
@XmlRootElement( name = "net" )
@XmlAccessorType( XmlAccessType.NONE )
public class NetPayload extends PocLinkablePayload implements PayloadWithPublicComments, PayloadWithOriginAses
{
    static final public String FIELD_NETHANDLE = "handle";
    static final public String FIELD_ORGHANDLE = "orgHandle";
    static final public String FIELD_PARENT_NETHANDLE = "parentNetHandle";
    static final public String FIELD_NETNAME = "netName";
    static final public String FIELD_VERSION = "version";
    static final public String FIELD_CUSTOMERHANDLE = "customerHandle";
    static final public String FIELD_CUSTOMER = "customer";
    static final public String FIELD_ORIGIN_AS = "originAS";
    static final public String WRAP_ORIGIN_AS = "originASes";
    static final public String FIELD_NET_BLOCK = "netBlock";
    static final public String WRAP_NET_BLOCK = "netBlocks";
    static final public String FIELD_START_ADDRESS = "startAddress";
    static final public String FIELD_END_ADDRESS = "endAddress";
    static final public String FIELD_REGDATE = "registrationDate";
    static final public String FIELD_MESSAGE = "message";
    static final public String FIELD_MESSAGES = "messages";
    static final public String FIELD_MESSAGE_REFERENCE = "messageReference";
    static final public String FIELD_MESSAGE_REFERENCES = "messageReferences";

    private String netHandle;
    private String orgHandle;
    private String customerHandle;
    private CustomerPayload customer;
    private String parentNetHandle;
    private String netName;
    private Integer version;
    private List<String> originAses;
    private List<NetBlockPayload> netBlocks;
    private MultilineTextPayload pubComment;
    private Date regDate;
    private List<MessagePayload> messages;
    private List<MessageReferencePayload> messageReferences;

    public NetPayload()
    {
        this.originAses = new ArrayList<>();
        this.netBlocks = new ArrayList<>();
    }

    @XmlElement( name = FIELD_NETHANDLE )
    @SystemGenerated
    public String getNetHandle()
    {
        return netHandle;
    }

    public void setNetHandle( String netHandle )
    {
        this.netHandle = netHandle;
    }

    @XmlElement( name = FIELD_NET_BLOCK )
    @XmlElementWrapper( name = WRAP_NET_BLOCK )
    @Immutable
    public List<NetBlockPayload> getNetBlocks()
    {
        return netBlocks;
    }

    public void setNetBlocks( List<NetBlockPayload> netBlocks )
    {
        this.netBlocks = netBlocks;
    }

    @XmlElement( name = FIELD_ORGHANDLE )
    @Immutable
    public String getOrgHandle()
    {
        return orgHandle;
    }

    public void setOrgHandle( String orgHandle )
    {
        this.orgHandle = orgHandle;
    }

    @XmlElement( name = FIELD_CUSTOMERHANDLE )
    @Immutable
    public String getCustomerHandle()
    {
        return customerHandle;
    }

    public void setCustomerHandle( String customerHandle )
    {
        this.customerHandle = customerHandle;
    }

    @XmlElement( name = FIELD_CUSTOMER )
    public CustomerPayload getCustomer()
    {
        return customer;
    }

    public void setCustomer( CustomerPayload customer )
    {
        this.customer = customer;
    }

    @XmlElement( name = FIELD_PARENT_NETHANDLE )
    @Immutable
    public String getParentNetHandle()
    {
        return parentNetHandle;
    }

    public void setParentNetHandle( String parentNetHandle )
    {
        this.parentNetHandle = parentNetHandle;
    }

    @XmlElement( name = FIELD_NETNAME )
    public String getNetName()
    {
        return netName;
    }

    public void setNetName( String netName )
    {
        this.netName = netName;
    }

    @XmlElement( name = FIELD_VERSION )
    @Immutable
    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( Integer version )
    {
        this.version = version;
    }

    @XmlElement( name = FIELD_ORIGIN_AS )
    @XmlElementWrapper( name = WRAP_ORIGIN_AS )
    public List<String> getOriginAses()
    {
        return originAses;
    }

    public void setOriginAses( List<String> originAses )
    {
        this.originAses = originAses;
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
        this.pubComment = MultilineTextPayload.makeMultilineText( pubComment );
    }

    /**
     * Set the specified net type on all NetBlockPayloads that are on this object.
     *
     * @param netType The desired net type
     */
    public void setNetTypeOnBlocks( NetBlockPayload.NetType netType )
    {
        for ( NetBlockPayload nb : this.getNetBlocks() )
        {
            nb.setType( netType );
        }
    }

    public NetBlockPayload.NetType getNetType()
    {
        if ( getNetBlocks().isEmpty() )
        {
            throw new RuntimeException( "No net type on net payload " + this.getNetHandle() );
        }
        else
        {
            return getNetBlocks().iterator().next().getType();
        }
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

    @Override
    public boolean equals( Object anotherPayload )
    {
        if ( !( anotherPayload instanceof NetPayload ) )
        {
            return false;
        }

        NetPayload toCompare = ( NetPayload ) anotherPayload;

        return ( StringUtils.equalsIgnoreCase( StringUtils.trim( toCompare.netHandle ), StringUtils.trim( this.netHandle ) )
                && StringUtils.equalsIgnoreCase( StringUtils.trim( toCompare.orgHandle ), StringUtils.trim( this.orgHandle ) )
                && StringUtils.equalsIgnoreCase( StringUtils.trim( toCompare.customerHandle ), StringUtils.trim( this.customerHandle ) )
                && StringUtils.equalsIgnoreCase( StringUtils.trim( toCompare.parentNetHandle ), StringUtils.trim( this.parentNetHandle ) )
                && StringUtils.equalsIgnoreCase( StringUtils.trim( toCompare.netName ), StringUtils.trim( this.netName ) )
                && ObjectUtils.equals( this.version, toCompare.version )
                && ObjectUtils.equals( this.pubComment, toCompare.pubComment )
                && originAsListsAreLogicallyEqual( this.originAses, toCompare.originAses )
                && CollectionUtils.equalContents( this.netBlocks, toCompare.netBlocks, new NetBlockPayload.NetBlockPayloadComparator() )
                && CollectionUtils.equalContents( this.pocLinks, toCompare.pocLinks, new PocLinkPayload.PocLinkPayloadComparator() )
        );
    }

    public boolean equalsExceptCommentsAndOriginAses( Object anotherPayload )
    {
        if ( !( anotherPayload instanceof NetPayload ) )
        {
            return false;
        }

        NetPayload toCompare = ( NetPayload ) anotherPayload;

        return ( StringUtils.equalsIgnoreCase( StringUtils.trim( toCompare.netHandle ), StringUtils.trim( this.netHandle ) )
                && StringUtils.equalsIgnoreCase( StringUtils.trim( toCompare.orgHandle ), StringUtils.trim( this.orgHandle ) )
                && StringUtils.equalsIgnoreCase( StringUtils.trim( toCompare.customerHandle ), StringUtils.trim( this.customerHandle ) )
                && StringUtils.equalsIgnoreCase( StringUtils.trim( toCompare.parentNetHandle ), StringUtils.trim( this.parentNetHandle ) )
                && StringUtils.equalsIgnoreCase( StringUtils.trim( toCompare.netName ), StringUtils.trim( this.netName ) )
                && ObjectUtils.equals( this.version, toCompare.version )
                && CollectionUtils.equalContents( this.netBlocks, toCompare.netBlocks, new NetBlockPayload.NetBlockPayloadComparator() )
                && CollectionUtils.equalContents( this.pocLinks, toCompare.pocLinks, new PocLinkPayload.PocLinkPayloadComparator() )
        );
    }

    @Override
    public int hashCode()
    {
        int result = 17;

        result = 37 * result
                + ( netHandle != null ? netHandle : "" ).hashCode();
        result = 37 * result
                + ( ( orgHandle != null ? orgHandle : "" ).hashCode() );
        result = 37 * result
                + ( ( customerHandle != null ? customerHandle : "" ).hashCode() );
        result = 37 * result
                + ( ( parentNetHandle != null ? parentNetHandle : "" ).hashCode() );
        result = 37 * result
                + ( ( orgHandle != null ? orgHandle : "" ).hashCode() );
        result = 37 * result
                + ( ( netName != null ? netName : "" ).hashCode() );
        result = 37 * result
                + String.valueOf( version ).hashCode();
        result = 37 * result
                + ( ( pubComment != null ) ? pubComment : "" ).hashCode();
        result = 37 * result
                + ( ( originAses != null ) ? originAses : "" ).hashCode();
        result = 37 * result
                + ( ( netBlocks != null ) ? netBlocks : "" ).hashCode();

        return result;
    }

    private static boolean originAsListsAreLogicallyEqual( List<String> originAses, List<String> moreOriginAses )
    {
        List<String> purgedOfText = removeTextFromOriginAses( originAses );
        List<String> morePurgedOfText = removeTextFromOriginAses( moreOriginAses );

        return CollectionUtils.equalContents( purgedOfText, morePurgedOfText );
    }

    private static List<String> removeTextFromOriginAses( List<String> original )
    {
        List<String> toReturn = new ArrayList<>();
        for ( String s : original )
        {
            toReturn.add( s.replaceAll( "AS", "" ).trim() );
        }
        return toReturn;
    }
}
