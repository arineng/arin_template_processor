package net.arin.tp.processor.exception;

import net.arin.tp.processor.utils.MessageBundle;

/**
 * This exception is used indicate during the execution of transformations that a scenario has been recognized as one
 * that requires ARIN review.
 */
public class TemplateRequiresReviewException extends TemplateException
{
    public TemplateRequiresReviewException()
    {
        super( MessageBundle.TEMPLATE_REQUIRES_REVIEW );
    }
}
