package net.arin.tp.processor.template;

import java.io.Serializable;

public class Attachment implements Serializable
{
    private String filename;
    private byte[] data;
    private String contentType;

    public Attachment()
    {
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename( String filename )
    {
        this.filename = filename;
    }

    public byte[] getData()
    {
        return data;
    }

    public void setData( byte[] data )
    {
        this.data = data;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType( String contentType )
    {
        this.contentType = contentType;
    }
}
