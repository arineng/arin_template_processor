package net.arin.tp.processor.exception;

import net.arin.tp.processor.utils.MessageBundle;

public class MultipleKeyException extends TemplateException
{
    public MultipleKeyException()
    {
        super( MessageBundle.MULTIPLE_API_KEY );
    }
}
