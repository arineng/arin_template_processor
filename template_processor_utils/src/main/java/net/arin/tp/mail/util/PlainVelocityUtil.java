package net.arin.tp.mail.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

public class PlainVelocityUtil
{
    static
    {
        Properties prop = new Properties();
        prop.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );
        prop.setProperty( "runtime.log.logsystem.log4j.logger", PlainSendMailUtil.class.getName() );
        prop.setProperty( "resource.loader", "class" );
        prop.setProperty( "runtime.log.invalid.references", "true" );
        prop.setProperty( "runtime.references.strict", "true" );
        prop.setProperty( "velocimacro.library", "" );
        prop.setProperty( "class.resource.loader.class", ClasspathResourceLoader.class.getName() );

        Velocity.init( prop );
    }

    public static VelocityEngine getVelocityInstance( Class clazz )
    {
        VelocityEngine ve = new VelocityEngine();

        // Bind the velocity logger to the system logger.
        ve.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );
        ve.setProperty( "runtime.log.logsystem.log4j.logger", clazz.getName() );

        ve.setProperty( "runtime.log.invalid.references", "true" );
        ve.setProperty( "runtime.references.strict", "true" );

        // Tell velocity to look for templates in the classpath.
        ve.setProperty( "resource.loader", "class" );
        ve.setProperty( "class.resource.loader.class", ClasspathResourceLoader.class.getName() );

        ve.setProperty( "velocimacro.library", "" );

        ve.init();

        return ve;
    }

    public String interpolateStream( String templateName, InputStream templateContent, Map<String, ?> propMap )
    {
        return interpolateStream( templateName, new InputStreamReader( templateContent ), propMap );
    }

    public String interpolateStream( String templateName, Reader templateContent, Map<String, ?> propMap )
    {
        VelocityEngine engine = PlainVelocityUtil.getVelocityInstance( PlainVelocityUtil.class );
        StringWriter bodyWriter = new StringWriter();
        engine.evaluate( getVelocityContext( propMap ), bodyWriter, templateName, templateContent );
        return bodyWriter.getBuffer().toString();
    }

    public String interpolateString( String templateName, String templateContent, Map<String, ?> propMap )
    {
        return interpolateStream( templateName, new StringReader( templateContent ), propMap );
    }

    public String interpolateFile( String templateFile, Map<String, ?> propMap )
    {
        VelocityContext context = getVelocityContext( propMap );
        Template template = Velocity.getTemplate( templateFile );
        return interpolate( template, context );
    }

    public String interpolate( Template t, VelocityContext velocityContext )
    {
        StringWriter writer = new StringWriter();
        t.merge( velocityContext, writer );
        return writer.toString();
    }

    public VelocityContext getVelocityContext()
    {
        return new VelocityContext();
    }

    /**
     * Populate a velocity context with values.
     *
     * @param map Put all items in the map into the Velocity Context
     * @return A velocity context with map items defined
     */
    public VelocityContext getVelocityContext( Map<String, ? extends Object> map )
    {
        VelocityContext velocityContext = getVelocityContext();

        if ( map != null )
        {
            for ( String key : map.keySet() )
            {
                Object value = map.get( key );
                velocityContext.put( key, value );
            }
        }

        return velocityContext;
    }
}
