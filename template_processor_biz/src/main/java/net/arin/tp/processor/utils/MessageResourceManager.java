package net.arin.tp.processor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.PropertyResourceBundle;

/**
 * Resolves the message resource bundle.
 */
public class MessageResourceManager
{
    private static final String MESSAGE_BUNDLE_RESOURCE = "template_validation_messages";

    private static MessageResourceManager instance = new MessageResourceManager();
    private PropertyResourceBundle bundle;

    private static final Logger log = LoggerFactory.getLogger( MessageResourceManager.class );

    public static MessageResourceManager getInstance()
    {
        return instance;
    }

    private MessageResourceManager()
    {
        try
        {
            bundle = ( PropertyResourceBundle ) PropertyResourceBundle.getBundle( MESSAGE_BUNDLE_RESOURCE );
        }
        catch ( Exception e )
        {
            log.error( "Failed to load message resource bundle", e );
            throw new RuntimeException( "Failed to load message resource bundle (" + MESSAGE_BUNDLE_RESOURCE + ") from classpath", e );
        }
    }

    public static String getMessage( String key, Object... params )
    {
        String messageTemplate = instance.bundle.getString( key );
        return MessageFormat.format( messageTemplate, params );
    }

    public static String getMessage( MessageBundle key, Object... params )
    {
        return getMessage( key.name(), params );
    }
}
