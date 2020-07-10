package net.arin.tp.processor.exception;

import net.arin.tp.processor.utils.MessageBundle;

public class MissingKeyException extends TemplateException
{
    public MissingKeyException()
    {
        super( MessageBundle.MISSING_API_KEY );
    }
}
