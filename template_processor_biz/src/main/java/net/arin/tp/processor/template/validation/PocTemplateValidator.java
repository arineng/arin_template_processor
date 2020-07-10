package net.arin.tp.processor.template.validation;

import net.arin.tp.processor.template.PocTemplateImpl;
import net.arin.tp.processor.template.TemplateImpl;
import org.apache.commons.lang.StringUtils;

/**
 * Validates the POC template.
 */
public class PocTemplateValidator extends Validator<PocTemplateImpl>
{
    @Override
    public boolean validate( PocTemplateImpl template )
    {
        TemplateImpl.Action action = template.getAction();

        if ( action == TemplateImpl.Action.MODIFY || action == TemplateImpl.Action.REMOVE )
        {
            return validateModifyTemplate( template );
        }

        return true;
    }

    private boolean validateModifyTemplate( PocTemplateImpl template )
    {
        if ( StringUtils.isBlank( template.getPocHandle() ) )
        {
            String errorMessage = getMessage( ValidationCode.MISSING_REQUIRED_ELEMENT, PocTemplateImpl.POC_HANDLE_LABEL );
            errorList.add( new ValidationError( ValidationCode.MISSING_REQUIRED_ELEMENT, errorMessage ) );
            return false;
        }

        return true;
    }
}
