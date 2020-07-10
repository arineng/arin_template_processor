package net.arin.tp.processor.template.validation;

import net.arin.tp.processor.template.TemplateImpl;
import net.arin.tp.processor.utils.MessageResourceManager;
import org.apache.commons.collections4.list.UnmodifiableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the base class for template validation. A subclass should be created for each template type that needs
 * validation. The specific validators are associated with the templates via the Resolver's template chain. The
 * validators are invoked via a Template Processor action (ValidateTemplate). Each validator must implement the
 * 'validate' method. If the validate method returns 'false', the errorList should be populated with at least one
 * ValidationError. The validationCode enum provides a means to make programmatic decisions. The errorMessage provides
 * a complete error message (from template_validation_messages.properties) that can be returned to the customer.
 * <p>
 * Note that as much validation as possible is done in Reg-RWS and not in the template_processor module, which is why
 * these validators have very little validation.
 *
 * @see net.arin.tp.processor.Resolver
 * @see net.arin.tp.processor.action.ValidateTemplate
 */
public abstract class Validator<T extends TemplateImpl>
{
    public enum ValidationCode
    {
        OK,
        MISSING_REQUIRED_ELEMENT
    }

    List<ValidationError> errorList = new ArrayList<>();

    protected static final Logger log = LoggerFactory.getLogger( Validator.class );

    /**
     * Performs the validation of the specified template. If the template is not valid, the errorList must contain at
     * least one ValidationError.
     *
     * @param template a template
     * @return true if the template is valid, false if it is not
     */
    public abstract boolean validate( T template );

    public List<ValidationError> getValidationErrors()
    {
        return UnmodifiableList.unmodifiableList( errorList );
    }

    /**
     * @return just the errorMessages as a list
     */
    public List<String> getValidationMessages()
    {
        List<String> messages = new ArrayList<>( errorList.size() );
        for ( ValidationError ve : errorList )
        {
            messages.add( ve.getErrorMessage() );
        }

        return messages;
    }

    /**
     * Check of the validation failed with a specified ValidationCode.
     */
    public boolean hasError( ValidationCode validationCode )
    {
        boolean hasError = false;

        for ( ValidationError ve : errorList )
        {
            if ( ve.getCode().equals( validationCode ) )
            {
                hasError = true;
            }
        }

        return hasError;
    }

    /**
     * Simple value object to correlate a validationCode with message.
     */
    public static class ValidationError
    {
        private ValidationCode code;
        private String errorMessage;

        ValidationError( ValidationCode code, String errorMessage )
        {
            this.code = code;
            this.errorMessage = errorMessage;
        }

        public ValidationCode getCode()
        {
            return code;
        }

        public String getErrorMessage()
        {
            return errorMessage;
        }
    }

    protected String getMessage( ValidationCode code, Object... params )
    {
        return MessageResourceManager.getMessage( code.toString(), params );
    }
}
