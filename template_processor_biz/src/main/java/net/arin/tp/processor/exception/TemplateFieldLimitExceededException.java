package net.arin.tp.processor.exception;

import net.arin.tp.processor.utils.MessageBundle;

public class TemplateFieldLimitExceededException extends TemplateException
{
    public TemplateFieldLimitExceededException( String field )
    {
        super( MessageBundle.TEMPLATE_FIELD_LIMIT_EXCEEDED, field );
    }
}
