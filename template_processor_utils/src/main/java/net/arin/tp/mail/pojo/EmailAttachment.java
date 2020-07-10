package net.arin.tp.mail.pojo;

import javax.activation.FileTypeMap;
import java.io.Serializable;

public class EmailAttachment implements Serializable
{
    static private FileTypeMap fileTypeMap = FileTypeMap.getDefaultFileTypeMap();

    private String name;
    private byte[] content;
    private String contentType;

    /**
     * Empty constructor and getters/setters needed for JSON conversion. Otherwise, use other provided constructors to
     * avoid setters.
     */
    public EmailAttachment()
    {
    }

    public EmailAttachment( String name, byte[] content, String contentType )
    {
        this.name = name;
        this.content = content.clone();
        this.contentType = contentType;
    }

    public EmailAttachment( byte[] attachment, String name )
    {
        this( name, attachment, fileTypeMap.getContentType( name ) );
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public byte[] getContent()
    {
        return content;
    }

    public void setContent( byte[] content )
    {
        this.content = content;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType( String contentType )
    {
        this.contentType = contentType;
    }

    @Override
    public String toString()
    {
        return name + "(" + contentType + " size:" + ( content == null ? 0 : content.length ) + ")";
    }
}
