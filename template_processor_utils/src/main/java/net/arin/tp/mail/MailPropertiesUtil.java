package net.arin.tp.mail;

import net.arin.tp.utils.SystemPropertiesHelper;
import net.arin.tp.utils.TemplateProcessorProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Properties;

public class MailPropertiesUtil
{
    private static final Logger log = LoggerFactory.getLogger( MailPropertiesUtil.class );

    private static String SCHEME_PROTOCOL = "smtp";
    private static int DEFAULT_PORT = 25;

    private static String JAVAX_MAIL_DEBUG = "mail.debug";
    private static String JAVAX_MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
    private static String JAVAX_MAIL_SMTP_PORT = "mail.smtp.port";
    private static String JAVAX_MAIL_HOST = "mail.host";
    private static String JAVAX_MAIL_START_TLS_ENABLED = "mail.smtp.starttls.enable";
    private static String JAVAX_MAIL_START_TLS_REQUIRED = "mail.smtp.starttls.required";
    private static String JAVAX_MAIL_SMTP_AUTH = "mail.smtp.auth";

    public static class UsernamePassword
    {
        public final String username;
        public final String password;

        public UsernamePassword( String userInfo )
        {
            final String[] tokens = StringUtils.defaultString( userInfo ).split( ":", 2 );

            // We need both a username and a password to do authentication.
            if ( tokens.length != 2 )
            {
                username = null;
                password = null;
                return;
            }

            // Similarly, both username and password have to be basically valid.
            if ( StringUtils.isBlank( tokens[0] ) || StringUtils.isBlank( tokens[1] ) )
            {
                username = null;
                password = null;
                return;
            }

            username = tokens[0];
            password = tokens[1];
        }
    }

    public static class MailProperties
    {
        public final Properties properties;
        public final UsernamePassword usernamePassword;

        public MailProperties( Properties properties, UsernamePassword usernamePassword )
        {
            this.properties = properties;
            this.usernamePassword = usernamePassword;
        }
    }

    public static MailProperties getProperties()
    {
        SystemPropertiesHelper props = new SystemPropertiesHelper();

        String mailServerUriString = props.lookupString( TemplateProcessorProperties.PROP_MAIL_SERVER_URI );
        boolean startTlsFlag = props.lookupBoolean( TemplateProcessorProperties.PROP_MAIL_START_TLS );
        boolean debugFlag = props.lookupBoolean( TemplateProcessorProperties.PROP_MAIL_DEBUG );

        URI mailServerUri = URI.create( mailServerUriString );

        if ( !mailServerUri.isAbsolute() )
        {
            throw new RuntimeException( String.format( "URI:[%s] is not absolute", mailServerUriString ) );
        }

        String scheme = mailServerUri.getScheme().toLowerCase();
        if ( StringUtils.isBlank( scheme ) )
        {
            throw new RuntimeException( String.format( "URI:[%s] has no scheme", mailServerUriString ) );
        }

        if ( !scheme.equals( SCHEME_PROTOCOL ) )
        {
            throw new RuntimeException( String.format( "URI:[%s] has wrong scheme [%s]", mailServerUriString, scheme ) );
        }

        String userInfo = mailServerUri.getUserInfo();
        UsernamePassword usernamePassword = new UsernamePassword( userInfo );

        int port = mailServerUri.getPort();
        if ( port == -1 )
        {
            port = DEFAULT_PORT;
        }

        String host = mailServerUri.getHost();
        if ( StringUtils.isBlank( host ) )
        {
            throw new RuntimeException( String.format( "URI:[%s] has no target host", mailServerUriString ) );
        }

        Properties properties = new Properties();

        properties.put( JAVAX_MAIL_TRANSPORT_PROTOCOL, scheme );
        properties.put( JAVAX_MAIL_HOST, host );
        properties.put( JAVAX_MAIL_SMTP_PORT, Integer.valueOf( port ).toString() );

        if ( usernamePassword.username != null )
        {
            properties.put( JAVAX_MAIL_SMTP_AUTH, "true" );
        }

        properties.put( JAVAX_MAIL_DEBUG, Boolean.valueOf( debugFlag ).toString() );
        properties.put( JAVAX_MAIL_START_TLS_ENABLED, Boolean.valueOf( startTlsFlag ).toString() );
        properties.put( JAVAX_MAIL_START_TLS_REQUIRED, Boolean.valueOf( startTlsFlag ).toString() );

        for ( Object key : properties.keySet() )
        {
            log.debug( String.format( "Mail property %s=%s", key, properties.get( key ) ) );
        }
        log.debug( "SMTP authentication for user=" + usernamePassword.username );

        return new MailProperties( properties, usernamePassword );
    }
}
