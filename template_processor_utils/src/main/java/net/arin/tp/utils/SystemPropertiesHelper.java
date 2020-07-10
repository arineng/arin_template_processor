package net.arin.tp.utils;

import org.springframework.util.SystemPropertyUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Provides some utility methods for dealing with system properties.
 * <p/>
 * The lookup methods in this class attempt to determine the data type being looked up, and return an appropriate
 * object. One interesting aspect of this is that sometimes the data type needs to be a Long but the input value is in
 * the Integer range. In cases like these, the input value can be appended with the "L" character thus forcing the value
 * to long.
 */
public class SystemPropertiesHelper
{
    /**
     * The place holder value that is considered to be null if found and {@link #noCheckForUnplacedValues} is true.
     */
    private final static String PLACE_VALUE_HERE = "***PLACE_VALUE_HERE***";

    private final static String ALLOW_DEFAULT_PROP = "arin.commons.allow_default_values";
    private final static String NO_CHECK_FOR_UNPLACED_PROP = "arin.commons.no_check_for_unplaced_values";

    public static String getProperty( String name )
    {
        return resolvePlaceholders( System.getProperty( name ) );
    }

    public static String getProperty( String name, String defaultValue )
    {
        return resolvePlaceholders( System.getProperty( name, defaultValue ) );
    }

    private static String resolvePlaceholders( String value )
    {
        if ( value != null && value.contains( "${" ) )
        {
            value = SystemPropertyUtils.resolvePlaceholders( value, true );
        }

        return value;
    }

    private boolean noCheckForUnplacedValues;
    private boolean allowForDefaultValues;

    public SystemPropertiesHelper()
    {
        allowForDefaultValues = Boolean.getBoolean( ALLOW_DEFAULT_PROP );
        noCheckForUnplacedValues = Boolean.getBoolean( NO_CHECK_FOR_UNPLACED_PROP );
    }

    public void setAllowForDefaultValues( boolean allowForDefaultValues )
    {
        this.allowForDefaultValues = allowForDefaultValues;
    }

    /**
     * A convenience method for helping in the conversion away from JNDI.
     */
    public void bind( String name, Object value )
    {
        System.setProperty( name, value.toString() );
    }

    /**
     * Convenience method for helping in the conversion away from JNDI. This is called mainly from the tests.
     */
    public void unbind( String name )
    {
        System.clearProperty( name );
    }

    public Integer lookupInteger( String name )
    {
        return lookup( Integer.class, name );
    }

    public Long lookupLong( String name )
    {
        return lookup( Long.class, name );
    }

    public Long lookupLong( String name, long defaultValue )
    {
        return lookup( Long.class, name, defaultValue );
    }

    public BigDecimal lookupBigDecimal( String name )
    {
        return lookup( BigDecimal.class, name );
    }

    public String lookupString( String name )
    {
        return lookup( String.class, name );
    }

    public Boolean lookupBoolean( String name )
    {
        return lookup( Boolean.class, name );
    }

    public Integer lookupInteger( String name, int defaultValue )
    {
        return lookup( Integer.class, name, defaultValue );
    }

    public String lookupString( String name, String defaultValue )
    {
        return lookup( String.class, name, defaultValue );
    }

    public String lookupDirectory( String name )
    {
        String directory = lookupString( name );
        return directory.endsWith( File.separator ) ? directory : directory + File.separator;
    }

    public Boolean lookupBoolean( String name, Boolean defaultValue )
    {
        return lookup( Boolean.class, name, defaultValue );
    }

    public <T> T lookup( Class<T> clazz, String name )
    {
        String value = getProperty( name, false );
        return parseValue( value, clazz, name );
    }

    public <T> T lookup( Class<T> clazz, String name, T defaultValue )
    {
        String value = getProperty( name, true );
        if ( value == null )
        {
            return defaultValue;
        }

        return parseValue( value, clazz, name );
    }

    private String getProperty( String name, boolean nullable )
    {
        String value = getProperty( name );

        if ( value == null )
        {
            if ( !nullable )
            {
                throw new RuntimeException( "System property '" + name + "' not found" );
            }
            if ( !allowForDefaultValues )
            {
                throw new RuntimeException( "System property '" + name + "' not found and default values are not allowed" );
            }
        }
        else if ( !noCheckForUnplacedValues && value.equalsIgnoreCase( PLACE_VALUE_HERE ) )
        {
            if ( !nullable )
            {
                throw new RuntimeException( "System property '" + name + "' is '" + PLACE_VALUE_HERE + "' (i.e., not set)" );
            }
            if ( !allowForDefaultValues )
            {
                throw new RuntimeException( "System property '" + name + "' is '" + PLACE_VALUE_HERE + "' (i.e., not set) and default values are not allowed" );
            }
        }

        return value;
    }

    @SuppressWarnings( "unchecked" )
    private <T> T parseValue( String value, Class<T> clazz, String name )
    {
        if ( clazz == String.class )
        {
            return ( T ) value;
        }
        else if ( clazz == Boolean.class )
        {
            if ( value.equalsIgnoreCase( Boolean.TRUE.toString() ) )
            {
                return ( T ) Boolean.TRUE;
            }
            else if ( value.equalsIgnoreCase( Boolean.FALSE.toString() ) )
            {
                return ( T ) Boolean.FALSE;
            }
            else
            {
                throw new RuntimeException( "System property '" + name + "' is not a boolean value" );
            }
        }
        else if ( clazz == Integer.class )
        {
            return ( T ) Integer.decode( value );
        }
        else if ( clazz == Long.class )
        {
            return ( T ) Long.decode( removeEndingL( value ) );
        }
        else if ( clazz == BigDecimal.class )
        {
            return ( T ) new BigDecimal( removeEndingL( value ) );
        }
        else
        {
            throw new UnsupportedOperationException( "System property of " + clazz + " type is not supported" );
        }
    }

    private String removeEndingL( String value )
    {
        String newValue = value.trim();
        return newValue.endsWith( "L" ) ? newValue.substring( 0, newValue.length() - 1 ) : newValue;
    }

    public static SortedMap<String, Object> getSystemProperties( String prefix )
    {
        SortedMap<String, Object> retval = new TreeMap<>();
        for ( Entry entry : System.getProperties().entrySet() )
        {
            String name = entry.getKey().toString();
            if ( name.startsWith( prefix ) )
            {
                retval.put( name, entry.getValue() );
            }
        }
        return retval;
    }
}
