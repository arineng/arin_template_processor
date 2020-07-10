package net.arin.tp.processor.template.validation;

import net.arin.tp.processor.template.OrgTemplateImpl;
import net.arin.tp.processor.template.TemplateImpl;
import org.apache.commons.lang.StringUtils;

/**
 * Validates the org template.
 */
public class OrgTemplateValidator extends Validator<OrgTemplateImpl>
{
    @Override
    public boolean validate( OrgTemplateImpl template )
    {
        TemplateImpl.Action action = template.getAction();

        if ( action == TemplateImpl.Action.MODIFY || action == TemplateImpl.Action.REMOVE )
        {
            return validateModifyTemplate( template );
        }

        return true;
    }

    private boolean validateModifyTemplate( OrgTemplateImpl template )
    {
        if ( StringUtils.isBlank( template.getOrgHandle() ) )
        {
            String errorMessage = getMessage( ValidationCode.MISSING_REQUIRED_ELEMENT, OrgTemplateImpl.ORG_HANDLE_LABEL );
            errorList.add( new ValidationError( ValidationCode.MISSING_REQUIRED_ELEMENT, errorMessage ) );
            return false;
        }

        return true;
    }
}
