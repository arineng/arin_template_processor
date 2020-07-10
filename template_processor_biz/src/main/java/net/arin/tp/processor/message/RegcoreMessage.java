package net.arin.tp.processor.message;

import java.io.Serializable;
import java.util.ArrayList;

public class RegcoreMessage implements Serializable
{
    private ArrayList<String> from = new ArrayList<>();
    private ArrayList<String> replyTo = new ArrayList<>();
    private ArrayList<String> to = new ArrayList<>();
    private ArrayList<String> cc = new ArrayList<>();
    private String subject = "";

    public ArrayList<String> getFrom()
    {
        return from;
    }

    public void setFrom( ArrayList<String> from )
    {
        this.from = from;
    }

    public ArrayList<String> getReplyTo()
    {
        return replyTo;
    }

    public void setReplyTo( ArrayList<String> replyTo )
    {
        this.replyTo = replyTo;
    }

    public ArrayList<String> getTo()
    {
        return to;
    }

    public void setTo( ArrayList<String> to )
    {
        this.to = to;
    }

    public ArrayList<String> getCc()
    {
        return cc;
    }

    public void setCc( ArrayList<String> cc )
    {
        this.cc = cc;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject( String subject )
    {
        this.subject = subject;
    }
}
