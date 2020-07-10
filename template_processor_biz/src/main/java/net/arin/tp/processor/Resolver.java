package net.arin.tp.processor;

import net.arin.tp.processor.template.IPV4NetModifyTemplateV4Impl;
import net.arin.tp.processor.template.IPV4NetModifyTemplateV5Impl;
import net.arin.tp.processor.template.IPV4ReallocateTemplateV4Impl;
import net.arin.tp.processor.template.IPV4ReallocateTemplateV5Impl;
import net.arin.tp.processor.template.IPV4ReassignDetailedTemplateV4Impl;
import net.arin.tp.processor.template.IPV4ReassignDetailedTemplateV5Impl;
import net.arin.tp.processor.template.IPV6NetModifyTemplateV4Impl;
import net.arin.tp.processor.template.IPV6NetModifyTemplateV5Impl;
import net.arin.tp.processor.template.IPV6ReallocateTemplateV4Impl;
import net.arin.tp.processor.template.IPV6ReallocateTemplateV5Impl;
import net.arin.tp.processor.template.IPV6ReassignDetailedTemplateV4Impl;
import net.arin.tp.processor.template.IPV6ReassignDetailedTemplateV5Impl;
import net.arin.tp.processor.template.OrgTemplateV4Impl;
import net.arin.tp.processor.template.OrgTemplateV5Impl;
import net.arin.tp.processor.template.PocTemplateV4Impl;
import net.arin.tp.processor.template.PocTemplateV5Impl;
import net.arin.tp.processor.template.ReassignSimpleTemplateV4Impl;
import net.arin.tp.processor.template.ReassignSimpleTemplateV5Impl;
import net.arin.tp.processor.template.Template;
import net.arin.tp.processor.template.TemplateImpl;
import net.arin.tp.processor.template.validation.OrgTemplateValidator;
import net.arin.tp.processor.template.validation.PassThroughValidator;
import net.arin.tp.processor.template.validation.PocTemplateValidator;
import net.arin.tp.processor.template.validation.Validator;
import net.arin.tp.processor.transform.NetModifyTransformer;
import net.arin.tp.processor.transform.OrgTransformer;
import net.arin.tp.processor.transform.PocTransformer;
import net.arin.tp.processor.transform.ReallocateTransformer;
import net.arin.tp.processor.transform.ReassignDetailedTransformer;
import net.arin.tp.processor.transform.ReassignSimpleTransformer;
import net.arin.tp.processor.transform.Transformer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds the correct template and transformer for a given email template. All new templates should be added to the
 * initialize method.
 */
public class Resolver
{
    private static final Logger log = LoggerFactory.getLogger( Resolver.class );
    private static final List<TemplateChain> templateChains = new ArrayList<>();

    /**
     * Map the email template objects to the correct transformer.
     */
    public static void initialize()
    {
        log.info( "Initializing Template Processor..." );
        templateChains.add( new TemplateChain( PocTemplateV4Impl.class, PocTransformer.class, PocTemplateValidator.class ) );
        templateChains.add( new TemplateChain( PocTemplateV5Impl.class, PocTransformer.class, PocTemplateValidator.class ) );

        templateChains.add( new TemplateChain( OrgTemplateV4Impl.class, OrgTransformer.class, OrgTemplateValidator.class ) );
        templateChains.add( new TemplateChain( OrgTemplateV5Impl.class, OrgTransformer.class, OrgTemplateValidator.class ) );

        templateChains.add( new TemplateChain( ReassignSimpleTemplateV4Impl.class, ReassignSimpleTransformer.class, PassThroughValidator.class ) );
        templateChains.add( new TemplateChain( ReassignSimpleTemplateV5Impl.class, ReassignSimpleTransformer.class, PassThroughValidator.class ) );

        templateChains.add( new TemplateChain( IPV4ReassignDetailedTemplateV4Impl.class, ReassignDetailedTransformer.class, PassThroughValidator.class ) );
        templateChains.add( new TemplateChain( IPV4ReassignDetailedTemplateV5Impl.class, ReassignDetailedTransformer.class, PassThroughValidator.class ) );
        templateChains.add( new TemplateChain( IPV6ReassignDetailedTemplateV4Impl.class, ReassignDetailedTransformer.class, PassThroughValidator.class ) );
        templateChains.add( new TemplateChain( IPV6ReassignDetailedTemplateV5Impl.class, ReassignDetailedTransformer.class, PassThroughValidator.class ) );

        templateChains.add( new TemplateChain( IPV4ReallocateTemplateV4Impl.class, ReallocateTransformer.class, PassThroughValidator.class ) );
        templateChains.add( new TemplateChain( IPV4ReallocateTemplateV5Impl.class, ReallocateTransformer.class, PassThroughValidator.class ) );
        templateChains.add( new TemplateChain( IPV6ReallocateTemplateV4Impl.class, ReallocateTransformer.class, PassThroughValidator.class ) );
        templateChains.add( new TemplateChain( IPV6ReallocateTemplateV5Impl.class, ReallocateTransformer.class, PassThroughValidator.class ) );

        templateChains.add( new TemplateChain( IPV4NetModifyTemplateV4Impl.class, NetModifyTransformer.class, PassThroughValidator.class ) );
        templateChains.add( new TemplateChain( IPV4NetModifyTemplateV5Impl.class, NetModifyTransformer.class, PassThroughValidator.class ) );

        templateChains.add( new TemplateChain( IPV6NetModifyTemplateV4Impl.class, NetModifyTransformer.class, PassThroughValidator.class ) );
        templateChains.add( new TemplateChain( IPV6NetModifyTemplateV5Impl.class, NetModifyTransformer.class, PassThroughValidator.class ) );
    }

    /**
     * Get a new instance of the template class for a given template name (e.g. 'ARIN-POC-5.0').
     *
     * @param emailTemplateName the name of the email template
     * @return a new instance of the template class associated with the given template name. Returns null if no template
     * class is found.
     */
    public static TemplateImpl getTemplate( String emailTemplateName )
    {
        // Must contain a major.minor version number with '.' as delimiter.
        if ( StringUtils.contains( emailTemplateName, '.' ) )
        {
            String majorVersionOnly = emailTemplateName.substring( 0, emailTemplateName.indexOf( "." ) + 1 );
            for ( TemplateChain templateChain : templateChains )
            {
                if ( templateChain.getName().startsWith( majorVersionOnly ) )
                {
                    return instantiate( templateChain.getTemplate() );
                }
            }
        }
        return null;
    }

    /**
     * Get a new instance of the transformer associated with the given template.
     *
     * @param template the template
     * @return A new instance of the transformer class associated with the given template. Returns null if no
     * transformer class is found.
     */
    public static Transformer getTransformer( Template template )
    {
        if ( template != null )
        {
            for ( TemplateChain templateChain : templateChains )
            {
                if ( templateChain.getTemplate().equals( template.getClass() ) )
                {
                    return instantiate( templateChain.getTransformer() );
                }
            }
        }
        return null;
    }

    /**
     * Get a new instance of the validator associated with the given template.
     *
     * @param template the template
     * @return A new instance of the validator class associated with the given template. Returns null if no validator
     * class is found.
     */
    public static Validator getValidator( Template template )
    {
        if ( template != null )
        {
            for ( TemplateChain templateChain : templateChains )
            {
                if ( templateChain.getTemplate().equals( template.getClass() ) )
                {
                    return instantiate( templateChain.getValidator() );
                }
            }
        }
        return null;
    }

    private static <T> T instantiate( Class<T> clazz )
    {
        try
        {
            return clazz.newInstance();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    public static class TemplateChain
    {
        private String name;
        private Class<? extends TemplateImpl> template;
        private Class<? extends Transformer> transformer;
        private Class<? extends Validator> validator;

        TemplateChain( Class<? extends TemplateImpl> template, Class<? extends Transformer> transformer, Class<? extends Validator> validator )
        {
            TemplateImpl t = instantiate( template );
            this.name = String.format( "%s-%s", t.getTemplateName(), t.getTemplateVersion() );
            this.template = template;
            this.transformer = transformer;
            this.validator = validator;

            log.info( "Registering template " + name + " to transformer " + transformer.getName() );
        }

        public String getName()
        {
            return name;
        }

        public Class<? extends TemplateImpl> getTemplate()
        {
            return template;
        }

        public Class<? extends Transformer> getTransformer()
        {
            return transformer;
        }

        public Class<? extends Validator> getValidator()
        {
            return validator;
        }
    }
}
