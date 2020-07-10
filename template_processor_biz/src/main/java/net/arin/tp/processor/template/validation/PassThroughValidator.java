package net.arin.tp.processor.template.validation;

import net.arin.tp.processor.template.TemplateImpl;

/**
 * Dummy validator that is always valid. Used for templates that don't have specific validation requirements since
 * every template must have a corresponding validator (see Resolver).
 */
public class PassThroughValidator extends Validator
{
    @Override
    public boolean validate( TemplateImpl template )
    {
        return true;
    }
}
