package net.arin.tp.processor.response;

import org.apache.velocity.VelocityContext;

import java.util.List;

public class GeneralFailureResponseContext extends AbstractResponseContext
{
    private List<String> errors;

    GeneralFailureResponseContext( List<String> errors )
    {
        this.errors = errors;
    }

    @Override
    public void setup( VelocityContext context )
    {
        context.put( "errors", errors );
    }

    @Override
    public String getTemplate()
    {
        return "email/generalFailure.vm";
    }

    @Override
    public ResponseType getResponseType()
    {
        return ResponseType.REJECTED;
    }
}
