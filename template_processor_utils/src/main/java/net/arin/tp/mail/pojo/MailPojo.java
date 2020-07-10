package net.arin.tp.mail.pojo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MailPojo implements Serializable
{
    private EmailAddress fromAddress;
    private Collection<EmailAddress> replyToAddresses;

    private Collection<EmailAddress> toAddresses;
    private Collection<EmailAddress> ccAddresses;
    private Collection<EmailAddress> bccAddresses;

    private String subject;
    private String body;
    private Collection<EmailAttachment> attachments;

    /**
     * Empty constructor and getters/setters needed for JSON conversion. Otherwise, use other provided constructors to
     * avoid setters.
     */
    public MailPojo()
    {
    }

    public MailPojo( FromInfo fromInfo, ToInfo toInfo, String subject, String body, List<EmailAttachment> attachments )
    {
        this.fromAddress = fromInfo.fromAddress;
        this.replyToAddresses = fromInfo.replyToAddresses;

        this.toAddresses = toInfo.toAddresses;
        this.ccAddresses = toInfo.ccAddresses;
        this.bccAddresses = toInfo.bccAddresses;

        this.subject = subject;
        this.body = body;
        this.attachments = Collections.unmodifiableCollection( attachments );
    }

    public MailPojo( FromInfo fromInfo, ToInfo toInfo, String subject, String body, EmailAttachment attachment )
    {
        this( fromInfo, toInfo, subject, body, Collections.singletonList( attachment ) );
    }

    public MailPojo( FromInfo fromInfo, ToInfo toInfo, String subject, String body )
    {
        this( fromInfo, toInfo, subject, body, Collections.<EmailAttachment>emptyList() );
    }

    public EmailAddress getFromAddress()
    {
        return fromAddress;
    }

    public void setFromAddress( EmailAddress fromAddress )
    {
        this.fromAddress = fromAddress;
    }

    public Collection<EmailAddress> getReplyToAddresses()
    {
        return replyToAddresses;
    }

    public void setReplyToAddresses( Collection<EmailAddress> replyToAddresses )
    {
        this.replyToAddresses = replyToAddresses;
    }

    public Collection<EmailAddress> getToAddresses()
    {
        return toAddresses;
    }

    public void setToAddresses( Collection<EmailAddress> toAddresses )
    {
        this.toAddresses = toAddresses;
    }

    public Collection<EmailAddress> getCcAddresses()
    {
        return ccAddresses;
    }

    public void setCcAddresses( Collection<EmailAddress> ccAddresses )
    {
        this.ccAddresses = ccAddresses;
    }

    public Collection<EmailAddress> getBccAddresses()
    {
        return bccAddresses;
    }

    public void setBccAddresses( Collection<EmailAddress> bccAddresses )
    {
        this.bccAddresses = bccAddresses;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject( String subject )
    {
        this.subject = subject;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody( String body )
    {
        this.body = body;
    }

    public Collection<EmailAttachment> getAttachments()
    {
        return attachments;
    }

    public void setAttachments( Collection<EmailAttachment> attachments )
    {
        this.attachments = attachments;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "To: " );
        builder.append( toAddresses );
        builder.append( "\nCc: " );
        builder.append( ccAddresses );
        builder.append( "\nBcc: " );
        builder.append( bccAddresses );
        builder.append( "\nFrom: " );
        builder.append( fromAddress );
        builder.append( "\nReplyTo: " );
        builder.append( replyToAddresses );
        builder.append( "\nSubject: " );
        builder.append( subject );
        builder.append( "\nBody:\n" );
        builder.append( body );
        if ( attachments != null && ( attachments.size() > 0 ) )
        {
            builder.append( "\nAttachments: " );
            builder.append( attachments );
        }

        return builder.toString();
    }
}
