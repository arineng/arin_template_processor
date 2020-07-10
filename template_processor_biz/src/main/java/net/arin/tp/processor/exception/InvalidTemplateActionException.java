package net.arin.tp.processor.exception;

import net.arin.tp.processor.utils.MessageBundle;

public class InvalidTemplateActionException extends TemplateException
{
    public InvalidTemplateActionException( String action )
    {
        super( MessageBundle.TEMPLATE_INVALID_ACTION, action );
    }
}
