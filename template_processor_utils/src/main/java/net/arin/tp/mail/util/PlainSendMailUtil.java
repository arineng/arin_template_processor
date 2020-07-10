package net.arin.tp.mail.util;

import net.arin.tp.mail.PlainSendMailSync;
import net.arin.tp.mail.SendMailBase;
import net.arin.tp.mail.pojo.EmailAttachment;
import net.arin.tp.mail.pojo.FromInfo;
import net.arin.tp.mail.pojo.MailPojo;
import net.arin.tp.mail.pojo.ToInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PlainSendMailUtil
{
    protected static final Logger log = LoggerFactory.getLogger( PlainSendMailUtil.class );

    public boolean renderAndSendSync( FromInfo fromInfo, ToInfo toInfo, String subject, String templateFile, Map<String, Object> propMap, EmailAttachment... emailAttachments )
    {
        return renderAndSend( new PlainSendMailSync(), new PlainVelocityUtil(), templateFile, fromInfo, toInfo, subject, propMap, emailAttachments );
    }

    final protected boolean renderAndSend( SendMailBase sendMailBase, PlainVelocityUtil plainVelocityUtil, String templateFile, FromInfo fromInfo, ToInfo toInfo, String subject, Map<String, Object> propMap, EmailAttachment... emailAttachments )
    {
        return renderAndSend( sendMailBase, fromInfo, toInfo, subject, plainVelocityUtil.interpolateFile( templateFile, propMap ), emailAttachments );
    }

    final protected boolean renderAndSend( SendMailBase sendMailBase, PlainVelocityUtil plainVelocityUtil, String templateName, String templateContent, FromInfo fromInfo, ToInfo toInfo, String subject, Map<String, Object> propMap, EmailAttachment... emailAttachments )
    {
        return renderAndSend( sendMailBase, fromInfo, toInfo, subject, plainVelocityUtil.interpolateString( templateName, templateContent, propMap ), emailAttachments );
    }

    final public boolean renderAndSend( SendMailBase sendMailBase, FromInfo fromInfo, ToInfo toInfo, String subject, String body, EmailAttachment... emailAttachments )
    {
        try
        {
            MailPojo mailPojo = new MailPojo(
                    fromInfo,
                    toInfo,
                    subject,
                    body,
                    listify( emailAttachments ) );

            log.debug( "Sending mail:" );
            log.debug( mailPojo.toString() );
            sendMailBase.send( mailPojo );
        }
        catch ( Exception e )
        {
            log.warn( "Could not send mail with subject " + subject, e );
            return false;
        }

        return true;
    }

    final public MailPojo render( PlainVelocityUtil plainVelocityUtil, String templateFile, FromInfo fromInfo,
                                  ToInfo toInfo, String subject, Map<String, ?> propMap,
                                  EmailAttachment... emailAttachments )
    {
        return render( fromInfo, toInfo, subject, plainVelocityUtil.interpolateFile( templateFile, propMap ),
                emailAttachments );
    }

    final public MailPojo render( FromInfo fromInfo, ToInfo toInfo, String subject, String body,
                                  EmailAttachment... emailAttachments )
    {
        return new MailPojo( fromInfo, toInfo, subject, body, listify( emailAttachments ) );
    }

    final public boolean send( SendMailBase sendMailBase, MailPojo mailPojo )
    {
        try
        {
            log.debug( "Sending mail:" );
            log.debug( mailPojo.toString() );
            sendMailBase.send( mailPojo );
        }
        catch ( Exception e )
        {
            log.warn( "Could not send mail with subject " + mailPojo.getSubject(), e );
            return false;
        }

        return true;
    }

    protected static List<EmailAttachment> listify( EmailAttachment[] emailAttachments )
    {
        List<EmailAttachment> emailAttachmentList;
        if ( emailAttachments != null )
        {
            emailAttachmentList = Arrays.asList( emailAttachments );
        }
        else
        {
            emailAttachmentList = Collections.emptyList();
        }
        return emailAttachmentList;
    }
}
