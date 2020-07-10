package net.arin.tp.processor.transform;

import net.arin.tp.processor.exception.RESTResponseFailure;
import net.arin.tp.processor.exception.TemplateException;
import net.arin.tp.processor.exception.TemplateRequiresReviewException;
import net.arin.tp.processor.message.TemplateMessage;
import net.arin.tp.processor.response.Response;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import java.util.List;

/**
 * The base of all the transformers. These transformers will take a parsed template and turn them into a series of REST
 * calls for Reg-RWS.
 * <p>
 * The singleRESTCallTransform method is where all the general error handling lives. It, in turn, calls the abstract
 * transform method which must be implemented by all of the various transformers.
 */
public abstract class Transformer
{
    static final String NONE = "NONE";
    protected static final Logger log = LoggerFactory.getLogger( Transformer.class );

    static
    {
        RegisterBuiltin.register( ResteasyProviderFactory.getInstance() );
    }

    Transformer()
    {
    }

    /**
     * This must be implemented in each inheriting class to provide the logic for transforming a template into REST
     * calls.
     *
     * @param message the parsed template message
     * @return a javax.mail.Message to be used as a response back to the customer
     * @throws TemplateException an exception representing the error that occurred in transforming the template
     */
    public abstract Message transform( TemplateMessage message )
            throws TemplateException;

    public final void singleRESTCallTransform( TemplateMessage message )
    {
        log.info( String.format( "api_key=%s template_name=%s - Starting template transform...",
                message.getApiKey(), message.getTemplate().getTemplateName() ) );

        try
        {
            Message response = transform( message );
            log.info( "Completed template transform" );
            send( response );
        }
        catch ( TemplateRequiresReviewException e )
        {
            log.info( "Template requires review" );
            throw e;
        }
        catch ( RESTResponseFailure e )
        {
            log.info( "Reg-RWS request failed: " + e.getPayload().getMessage() );
            Message response = e.toResponse( message );
            send( response );
        }
        catch ( TemplateException e )
        {
            log.info( "Transforming template to Reg-RWS request failed: " + e.getMessage() );
            Message response = e.toResponse( message );
            send( response );
        }
        catch ( Exception e )
        {
            log.error( "Error occurred while processing template", e );
            Message response = Response.generalFailure( message,
                    "An error occurred while processing your template. Please contact us." );
            send( response );
        }
    }

    private void send( Message response )
    {
        try
        {
            if ( response != null )
            {
                Response.send( response );
            }
        }
        catch ( Exception e )
        {
            log.error( "Error occurred when sending response message", e );
        }
    }

    static Boolean isNone( String string )
    {
        string = ( string == null ) ? "" : string.trim();
        return NONE.equals( string );
    }

    static Boolean hasNone( List<String> array )
    {
        for ( String string : array )
        {
            if ( isNone( string ) )
            {
                return true;
            }
        }

        return false;
    }

    static String normalizeStateProv( String stateProv )
    {
        if ( stateProv == null )
        {
            return null;
        }

        if ( stateProv.length() == 2 )
        {
            return stateProv.toUpperCase();
        }

        return stateProv;
    }
}
