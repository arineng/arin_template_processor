package net.arin.tp.mail;

import net.arin.tp.mail.pojo.MailPojo;

public interface SendMailBase
{
    void send( MailPojo mailPojo );
}
