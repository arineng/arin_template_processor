package net.arin.tp.processor.exception;

import net.arin.tp.processor.utils.MessageBundle;

public class AmbiguousFieldException extends TemplateException
{
    public AmbiguousFieldException( String field )
    {
        super( MessageBundle.AMBIGUOUS_VALUES, field );
    }
}
