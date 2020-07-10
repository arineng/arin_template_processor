package net.arin.tp.processor.transform;

import net.arin.tp.api.payload.PayloadWithOriginAses;
import net.arin.tp.api.payload.PayloadWithPublicComments;
import net.arin.tp.api.payload.PocLinkPayload;
import net.arin.tp.api.payload.PocLinkablePayload;
import net.arin.tp.processor.exception.AmbiguousFieldException;
import net.arin.tp.processor.template.TemplateWithOriginAses;
import net.arin.tp.processor.template.TemplateWithPublicComments;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Utility class used by transformers that includes functionality related to values on multiple types of templates.
 */
public class TransformerAssistant
{
    static void transformPocs( PocLinkPayload.Function pocType, PocLinkablePayload payload, List<String> templatePocs,
                               String field )
    {
        // Currently, for POC handles, "NONE" values are respected when they are sent by themselves. In the odd where
        // "NONE" is sent as part of a list of POC handles (for any particular POC type), such a "NONE" value will cause
        // an Exception.

        if ( templatePocs.size() == 1 && Transformer.isNone( templatePocs.get( 0 ) ) )
        {
            payload.clearPocLinksForFunction( pocType );
        }
        else if ( templatePocs.size() > 0 )
        {
            payload.clearPocLinksForFunction( pocType );

            for ( String pocHandle : templatePocs )
            {
                if ( !Transformer.isNone( pocHandle ) && !StringUtils.isEmpty( pocHandle ) )
                {
                    switch ( pocType )
                    {
                        case T:
                            payload.addTechPoc( pocHandle );
                            break;
                        case N:
                            payload.addNocPoc( pocHandle );
                            break;
                        case AB:
                            payload.addAbusePoc( pocHandle );
                            break;
                        default:
                            throw new IllegalArgumentException( "This method may only be used with TECH, ABUSE, and NOC POC types." );
                    }
                }
                if ( Transformer.isNone( pocHandle ) )
                {
                    throw new AmbiguousFieldException( field );
                }
            }
        }
    }

    static void transformOriginAses( TemplateWithOriginAses template, PayloadWithOriginAses payload )
    {
        if ( !template.getOriginAses().isEmpty() )
        {
            boolean noneFound = false;
            for ( String as : template.getOriginAses() )
            {
                if ( Transformer.isNone( as ) )
                {
                    noneFound = true;
                }
            }
            // If *any* of the AS values is 'none' then wipe them all.
            if ( noneFound )
            {
                payload.getOriginAses().clear();
            }
            // If there is no 'none', then wipe the list and add what is in the template over to the payload.
            else
            {
                payload.getOriginAses().clear();
                payload.getOriginAses().addAll( template.getOriginAses() );
            }
        }
    }

    static void transformPublicComments( TemplateWithPublicComments publicCommentsTemplate, PayloadWithPublicComments payload )
    {
        if ( publicCommentsTemplate.getPublicComments().size() == 1 && Transformer.isNone( publicCommentsTemplate.getPublicComments().get( 0 ) ) )
        {
            payload.setMultilinePublicComments( null );
        }
        else if ( publicCommentsTemplate.getPublicComments().size() > 0 )
        {
            boolean update = false;
            for ( String comment : publicCommentsTemplate.getPublicComments() )
            {
                if ( !StringUtils.isEmpty( comment ) )
                {
                    update = true;
                }
            }

            // If there is one comment string that's not empty, join them all and and update the payload.
            if ( update )
            {
                payload.setPublicComments( StringUtils.join( publicCommentsTemplate.getPublicComments(), "\n" ) );
            }
        }
    }
}
