package net.arin.tp.processor.exception;

import net.arin.tp.api.payload.ErrorPayload;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import org.jboss.resteasy.client.ClientResponseFailure;

import javax.mail.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RESTResponseFailure extends TemplateException
{
    private ErrorPayload payload = null;

    private RESTResponseFailure()
    {
        super( "An error payload was received from the REST host." );
    }

    public RESTResponseFailure( ClientResponseFailure crf )
    {
        this();

        this.payload = ( ErrorPayload ) crf.getResponse().getEntity( ErrorPayload.class );

        crf.getResponse().releaseConnection();
    }

    public ErrorPayload getPayload()
    {
        return payload;
    }

    public void setPayload( ErrorPayload payload )
    {
        this.payload = payload;
    }

    protected Map<String, String> getTemplateMapping( TemplateMessage template )
    {
        return template.getTemplate().getXmlToFieldKeyMapping();
    }

    @Override
    public Message toResponse( TemplateMessage template )
    {
        Message response;

        // If the message contains component errors, put those messages in.
        if ( payload.getComponentErrors().size() > 0 )
        {
            Map<String, String> mapping = getTemplateMapping( template );

            List<String> errors = new ArrayList<>();
            for ( ErrorPayload.ComponentErrorPayload error : payload.getComponentErrors() )
            {
                String componentName = error.getName();
                StringBuilder message = new StringBuilder();

                // We'll default to using the component name as the field name. In case we ever miss one the user will
                // still receive something back. If we do find a mapping, we'll return the template field key instead.
                if ( mapping.containsKey( componentName ) )
                {
                    message.append( mapping.get( componentName ) );
                    if ( message.length() > 0 )
                    {
                        message.append( " " );
                    }
                    message.append( error.getMessage() );
                }
                else
                {
                    message.append( componentName );
                    message.append( ": " );
                    message.append( error.getMessage() );
                }

                errors.add( message.toString() );
            }

            response = Response.generalFailure( template, errors );
        }
        else
        {
            // Otherwise, just use the normal error message.
            response = Response.generalFailure( template, payload.getMessage() );
        }

        return response;
    }
}
