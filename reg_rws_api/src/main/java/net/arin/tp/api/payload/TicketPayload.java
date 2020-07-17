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
 * The TicketPayload structure provides access to the ticket related data available via templates or ARIN Online. Not
 * all service calls can be auto-processed. Some will required intervention by an ARIN service department. In these
 * cases, this payload will be used to provide you details regarding your request.
 * </p>
 *
 * <p>
 * The body of the payload will vary depending on the action requested. Message elements will not be included if the
 * ticket has no messages or a GetTicketSummary or GetTicketSummaries call is requested.
 * </p>
 *
 * <p>
 * When performing the GetTicketDetails or GetTickets calls as specified in the Methods documentation, the following
 * payload will be returned when the msgRefs parameter is specified as 'true'.
 * </p>
 *
 * <p>
 * <h3>PAYLOAD WHEN MSGREFS PARAMETER IS 'TRUE' WHEN GETTING TICKET DETAILS</h3>
 * <pre>
 * &lt;ticket xmlns=&quot;http://www.arin.net/regrws/core/v1&quot; xmlns:ns2=&quot;http://www.arin.net/regrws/messages/v1&quot;&gt;
 *     &lt;messageReferences&gt;
 *         &lt;messageReference&gt;
 *             &lt;messageId&gt;&lt;/messageId&gt;
 *             &lt;attachmentReferences&gt;
 *                 &lt;attachmentReference&gt;
 *                     &lt;attachmentFilename&gt;&lt;/attachmentFilename&gt;
 *                     &lt;attachmentId&gt;&lt;/attachmentId&gt;
 *                 &lt;/attachmentReference&gt;
 *             &lt;/attachmentReferences&gt;
 *         &lt;/messageReference&gt;
 *     &lt;/messageReferences&gt;
 *     &lt;createdDate&gt;&lt;/createdDate&gt;
 *     &lt;ticketNo&gt;&lt;/ticketNo&gt;
 *     &lt;resolvedDate&gt;&lt;/resolvedDate&gt;
 *     &lt;closedDate&gt;&lt;/closedDate&gt;
 *     &lt;updatedDate&gt;&lt;/updatedDate&gt;
 *     &lt;webTicketType&gt;>[POC_RECOVERY, QUESTION, ASSOCIATIONS_REPORT, REASSIGNMENT_REPORT, ORG_CREATE, EDIT_ORG_NAME, ORG_RECOVERY, TRANSFER_LISTING_SERVICE,
 *     IPV4_SIMPLE_REASSIGN, IPV4_DETAILED_REASSIGN, IPV4_REALLOCATE, IPV6_DETAILED_REASSIGN, IPV6_REALLOCATE, NET_DELETE_REQUEST, ISP_IPV4_REQUEST, ISP_IPV6_REQUEST,
 *     CREATE_RESOURCE_CERTIFICATE, CREATE_ROA, END_USER_IPV4_REQUEST, END_USER_IPV6_REQUEST, ASN_REQUEST, EDIT_BILLING_CONACT_INFO, ANY]&lt;/webTicketType&gt;
 *     &lt;webTicketStatus&gt;[PENDING_CONFIRMATION, PENDING_REVIEW, PENDING_CUSTOMER_REVIEW ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED, APPROVED, ANY, ANY_OPEN]&lt;/webTicketStatus&gt;
 *     &lt;webTicketResolution&gt;[ACCEPTED, DENIED, ABANDONED, ANSWERED, PROCESSED, DUPLICATE, WITHDRAWN, UNSUCCESSFUL, OTHER]&lt;/webTicketResolution&gt;
 * &lt;/ticket&gt;
 * </pre>
 * </p>
 *
 * <p>
 * <h3>EXAMPLE PAYLOAD WHEN MSGREFS PARAMETER IS 'TRUE' WHEN GETTING TICKET DETAILS</h3>
 * <pre>
 * &lt;ticket xmlns=&quot;http://www.arin.net/regrws/core/v1&quot;&gt;
 *     &lt;messageReferences&gt;
 *         &lt;messageReference&gt;
 *             &lt;messageId&gt;MESSAGEID&lt;/messageId&gt;
 *             &lt;attachmentReferences&gt;
 *                 &lt;attachmentReference&gt;
 *                     &lt;attachmentFilename&gt;ATTACHMENTFILENAME&lt;/attachmentFilename&gt;
 *                     &lt;attachmentId&gt;ATTACHMENTID&lt;/attachmentId&gt;
 *                 &lt;/attachmentReference&gt;
 *             &lt;/attachmentReferences&gt;
 *         &lt;/messageReference&gt;
 *     &lt;/messageReferences&gt;
 *     &lt;createdDate&gt;Tue Feb 28 17:41:17 EST 2012&lt;/createdDate&gt;
 *     &lt;ticketNo&gt;TICKETNO&lt;/ticketNo&gt;
 *     &lt;resolvedDate&gt;Tue Feb 28 17:41:17 EST 2012&lt;/resolvedDate&gt;
 *     &lt;closedDate&gt;Tue Feb 28 17:41:17 EST 2012&lt;/closedDate&gt;
 *     &lt;updatedDate&gt;Tue Feb 28 17:41:17 EST 2012&lt;/updatedDate&gt;
 *     &lt;webTicketType&gt;POC_RECOVERY&lt;/webTicketType&gt;
 *     &lt;webTicketStatus&gt;PENDING_CONFIRMATION&lt;/webTicketStatus&gt;
 *     &lt;webTicketResolution&gt;ACCEPTED&lt;/webTicketResolution&gt;
 * &lt;/ticket&gt;
 * </pre>
 * </p>
 *
 * <p>
 * When performing the GetTicketDetails or GetTickets calls as specified in the Methods documentation, the following
 * payload will be returned when the msgRefs parameter is specified as 'false' or left unspecified.
 * </p>
 */
@XmlRootElement( name = "ticket" )
@XmlAccessorType( XmlAccessType.NONE )
public class TicketPayload implements Payload
{
    static final public String FIELD_TICKETNO = "ticketNo";
    static final public String FIELD_ORGHANDLE = "orgHandle";
    static final public String FIELD_SHARED = "shared";
    static final public String FIELD_CREATEDDATE = "createdDate";
    static final public String FIELD_RESOLVEDDATE = "resolvedDate";
    static final public String FIELD_UPDATEDDATE = "updatedDate";
    static final public String FIELD_CLOSEDDATE = "closedDate";
    static final public String FIELD_WEBTICKETTYPE = "webTicketType";
    static final public String FIELD_WEBTICKETSTATUS = "webTicketStatus";
    static final public String FIELD_WEBTICKETRESOLUTION = "webTicketResolution";
    static final public String FIELD_MESSAGE = "message";
    static final public String FIELD_MESSAGES = "messages";
    static final public String FIELD_MESSAGE_REFERENCE = "messageReference";
    static final public String FIELD_MESSAGE_REFERENCES = "messageReferences";

    static final public String NAMESPACE4 = "http://www.arin.net/regrws/shared-ticket/v1";

    private String ticketNo;
    private String orgHandle;
    private Boolean shared;
    private Date createdDate;
    private Date resolvedDate;
    private Date closedDate;
    private Date updatedDate;
    private WebTicketType webTicketType;
    private WebTicketStatus webTicketStatus;
    private WebTicketResolution webTicketResolution;
    private List<MessagePayload> messages;
    private List<MessageReferencePayload> messageReferences;

    /**
     * Supported ticket types for the ticket services.
     */
    public enum WebTicketType
    {
        /**
         * Type denotes a POC recovery ticket.
         */
        POC_RECOVERY,

        /**
         * Type denotes a question ticket.
         */
        QUESTION,

        /**
         * Type denotes a background report process for associations.
         */
        ASSOCIATIONS_REPORT,

        /**
         * Type denotes a background report process for reassignments.
         */
        REASSIGNMENT_REPORT,

        /**
         * Type denotes a background report process for reassignments.
         */
        USER_REASSIGNMENT_REPORT,

        /**
         * Type denotes a background report process for a WhoWas report.
         */
        WHOWAS_REPORT,

        /**
         * Type denotes a WhoWas access request.
         */
        WHOWAS_ACCESS,

        /**
         * Type denotes an organization create ticket.
         */
        ORG_CREATE,

        /**
         * Type denotes an organization name change ticket.
         */
        EDIT_ORG_NAME,

        /**
         * Type denotes an organization recovery ticket.
         */
        ORG_RECOVERY,

        /**
         * Type denotes a transfer listing service ticket.
         */
        TRANSFER_LISTING_SERVICE,
        /**
         * Type denotes an IPv4 simple reassignment request.
         */
        IPV4_SIMPLE_REASSIGN,

        /**
         * Type denotes an IPv4 detailed reassignment request.
         */
        IPV4_DETAILED_REASSIGN,

        /**
         * Type denotes an IPv4 reallocation request.
         */
        IPV4_REALLOCATE,

        /**
         * Type denotes an IPv6 reassignment request.
         */
        IPV6_DETAILED_REASSIGN,

        /**
         * Type denotes an IPv6 reallocation request.
         */
        IPV6_REALLOCATE,

        /**
         * Type denotes a background process for net deletion.
         */
        NET_DELETE_REQUEST,

        /**
         * Type denotes ISP IPV4 request.
         */
        ISP_IPV4_REQUEST,
        /**
         * Type denotes ISP IPV6 request.
         */
        ISP_IPV6_REQUEST,
        /**
         * Type denotes an end user IPV4 request.
         */
        END_USER_IPV4_REQUEST,
        /**
         * Type denotes an end user IPV6 request.
         */
        END_USER_IPV6_REQUEST,
        /**
         * Type denotes an ASN request.
         */
        ASN_REQUEST,

        /**
         * Type denotes create hosted resource certificate request.
         */
        CREATE_HOSTED_RESOURCE_CERTIFICATE,

        /**
         * Type denotes updown identity exchange (create updown resource certificate request).
         */
        UPDOWN_IDENTITY_EXCHANGE,

        /**
         * Type denotes updown identity update.
         */
        UPDATE_UPDOWN_IDENTITY,

        /**
         * Type denotes a create ROA request.
         */
        CREATE_ROA,

        /**
         * Type denotes a Billing Contact Info update request.
         */
        EDIT_BILLING_CONTACT_INFO,

        /**
         * Type denotes a paid membership request.
         */
        PAID_MEMBERSHIP_REQUEST,

        /**
         * Type denotes Transfer 8.2 Receipt request.
         */
        TRANSFER_RECIPIENT_82,

        /**
         * Type denotes Transfer 8.3 Source request.
         */
        TRANSFER_SOURCE_83,

        /**
         * Type denotes Transfer 8.3 Receipt request.
         */
        TRANSFER_RECIPIENT_83,

        /**
         * Type denotes Transfer 8.4 Receipt request.
         */
        TRANSFER_SOURCE_84,

        /**
         * Type denotes Transfer 8.4 Source request
         */
        TRANSFER_RECIPIENT_84,
        /**
         * Type denotes a Transfer Pre-Approval request.
         */
        TRANSFER_PREAPPROVAL,

        /**
         * A value to represent all possible types. This value should be used when searching tickets only.
         */
        ANY
    }

    /**
     * Supported ticket statuses for tickets returned by the ticket services.
     */
    public enum WebTicketStatus
    {
        /**
         * An e-mail was sent to you requesting you confirm your e-mail and/or intended action.
         */
        PENDING_CONFIRMATION,

        /**
         * Your ticket is pending review by an ARIN service department.
         */
        PENDING_REVIEW,

        /**
         * Your ticket is pending review by the customer the ticket was created on behalf of.
         */
        PENDING_CUSTOMER_REVIEW,

        /**
         * Your ticket has been assigned to an individual within the ARIN organization.
         */
        ASSIGNED,

        /**
         * Your ticket is being actively worked on by an individual within the ARIN organization. It may be waiting a
         * response from you.
         */
        IN_PROGRESS,

        /**
         * Your ticket is in a waiting list for IPv4 address space. The ticket will advance once an address space you
         * requested becomes available.
         */
        WAIT_LIST,

        /**
         * Your ticket has been resolved. You may request further assistance if it has not been completed to your
         * satisfaction.
         */
        RESOLVED,

        /**
         * Your ticket has been closed and all work completed.
         */
        CLOSED,

        /**
         * Your ticket has been approved.
         */
        APPROVED,

        /**
         * A value to represent all ticket statuses. This value should be used when searching tickets only.
         */
        ANY,

        /**
         * A value to represent all ticket statuses other than CLOSED. This value should be used when searching tickets
         * only.
         */
        ANY_OPEN
    }

    /**
     * Supported resolutions for tickets. This will be the state of your ticket when it's been "resolved."
     */
    public enum WebTicketResolution
    {
        /**
         * Your changes have been accepted by ARIN. This resolution applies to edits, profile updates, etc.
         */
        ACCEPTED,

        /**
         * Your changes have been denied by ARIN. This resolution applies to edits, profile updates, etc.
         */
        DENIED,

        /**
         * The ticket has been closed because it sat idle, with no interaction from the customer, for too long.
         */
        ABANDONED,

        /**
         * Your question has been answered. This resolution only applies to questions.
         */
        ANSWERED,

        /**
         * A long running task has completed execution. This applies to report generation, deletes of large networks,
         * etc.
         */
        PROCESSED,

        /**
         * Your request was duplicate to a previously opened ticket.
         */
        DUPLICATE,

        /**
         * Your request was closed by you.
         */
        WITHDRAWN,

        /**
         * Your request was unsuccessful.
         */
        UNSUCCESSFUL,

        /**
         * If the final state of your ticket doesn't fall into the above categories, it will be marked as OTHER.
         */
        OTHER
    }

    /**
     * Used to retrieve the ticket number of your request.
     */
    @XmlElement( name = FIELD_TICKETNO )
    @SystemGenerated
    public String getTicketNo()
    {
        return ticketNo;
    }

    /**
     * Used to set the ticket number of your request.
     */
    public void setTicketNo( String ticketNo )
    {
        this.ticketNo = ticketNo;
    }

    /**
     * Used to retrieve the org handle of your request.
     */
    @XmlElement( name = FIELD_ORGHANDLE, namespace = NAMESPACE4 )
    @Immutable
    public String getOrgHandle()
    {
        return orgHandle;
    }

    public void setOrgHandle( String orgHandle )
    {
        this.orgHandle = orgHandle;
    }

    /**
     * Used to get if this ticket is shared.
     */
    @XmlElement( name = FIELD_SHARED, namespace = NAMESPACE4 )
    @Immutable
    public Boolean isShared()
    {
        return shared;
    }

    public void setShared( Boolean shared )
    {
        this.shared = shared;
    }

    /**
     * Used to retrieve the date your request was created in our system.
     */
    @XmlElement( name = FIELD_CREATEDDATE )
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
     * Used to retrieve the date your ticket was resolved.
     */
    @XmlElement( name = FIELD_RESOLVEDDATE )
    @SystemGenerated
    public Date getResolvedDate()
    {
        return resolvedDate;
    }

    public void setResolvedDate( Date resolvedDate )
    {
        this.resolvedDate = resolvedDate;
    }

    /**
     * Used to retrieve the date your ticket was closed.
     */
    @XmlElement( name = FIELD_CLOSEDDATE )
    @SystemGenerated
    public Date getClosedDate()
    {
        return closedDate;
    }

    public void setClosedDate( Date closedDate )
    {
        this.closedDate = closedDate;
    }

    /**
     * Used to retrieve the date your ticket was last updated.
     */
    @XmlElement( name = FIELD_UPDATEDDATE )
    @SystemGenerated
    public Date getUpdatedDate()
    {
        return updatedDate;
    }

    public void setUpdatedDate( Date updatedDate )
    {
        this.updatedDate = updatedDate;
    }

    /**
     * Used to retrieve the ticket type of your request.
     */
    @XmlElement( name = FIELD_WEBTICKETTYPE )
    @SystemGenerated
    public WebTicketType getWebTicketType()
    {
        return webTicketType;
    }

    public void setWebTicketType( WebTicketType webTicketType )
    {
        this.webTicketType = webTicketType;
    }

    /**
     * Used to retrieve the status of your request.
     */
    @XmlElement( name = FIELD_WEBTICKETSTATUS )
    public WebTicketStatus getWebTicketStatus()
    {
        return webTicketStatus;
    }

    public void setWebTicketStatus( WebTicketStatus webTicketStatus )
    {
        this.webTicketStatus = webTicketStatus;
    }

    /**
     * Used to retrieve the resolution of your request.
     */
    @XmlElement( name = FIELD_WEBTICKETRESOLUTION )
    @SystemGenerated
    public WebTicketResolution getWebTicketResolution()
    {
        return webTicketResolution;
    }

    public void setWebTicketResolution( WebTicketResolution webTicketResolution )
    {
        this.webTicketResolution = webTicketResolution;
    }

    /**
     * Retrieves any messages associated with this ticket.
     */
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

    /**
     * Retrieves any messages references associated with this ticket.
     */
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
}
