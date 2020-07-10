package net.arin.tp.processor.exception;

import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.utils.MessageBundle;
import net.arin.tp.processor.utils.MessageResourceManager;

import javax.mail.Message;

public class TemplateException extends RuntimeException
{
    public TemplateException( MessageBundle message, Object... params )
    {
        super( MessageResourceManager.getMessage( message, params ) );
    }

    public TemplateException( String message )
    {
        super( message );
    }

    public Message toResponse( TemplateMessage template )
    {
        return Response.generalFailure( template, getMessage() );
    }
}
