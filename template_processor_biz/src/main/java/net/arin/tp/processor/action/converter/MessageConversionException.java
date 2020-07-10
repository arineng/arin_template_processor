package net.arin.tp.processor.action.converter;

import net.arin.tp.processor.message.TemplateMessage;

public class MessageConversionException extends Exception
{
    private TemplateMessage template;

    MessageConversionException( TemplateMessage template, String s )
    {
        super( s );
        setTemplate( template );
    }

    public TemplateMessage getTemplate()
    {
        return template;
    }

    public void setTemplate( TemplateMessage template )
    {
        this.template = template;
    }
}
