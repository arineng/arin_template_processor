package net.arin.tp.processor.action;

import net.arin.tp.processor.Resolver;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import net.arin.tp.processor.template.Template;
import net.arin.tp.processor.template.TemplateImpl;
import net.arin.tp.processor.template.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Template Processor action to invoke validation on each template.
 */
public class ValidateTemplate
{
    private static final Logger log = LoggerFactory.getLogger( ValidateTemplate.class );

    @SuppressWarnings( "unchecked" )
    public boolean validate( TemplateMessage message )
    {
        log.debug( "Validating template..." );
        Template template = message.getTemplate();

        Validator validator = Resolver.getValidator( template );
        if ( validator == null )
        {
            log.debug( "No validator has been defined for template '" + template.getTemplateName() +
                    "'; no validation will be performed" );
            return true;
        }

        if ( !validator.validate( ( TemplateImpl ) template ) )
        {
            log.debug( "Validation failed" );
            javax.mail.Message response = Response.generalFailure( message, validator.getValidationMessages() );
            Response.send( response );
            return false;
        }

        log.debug( "Validation succeeded" );
        return true;
    }
}
