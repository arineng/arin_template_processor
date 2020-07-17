package net.arin.tp.api.utils;

import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class contains utility methods that operate on IP addresses.
 */
public class IPUtils
{
    private IPUtils()
    {
    }

    /**
     * This method will attempt to compare two strings that we believe to be IP addresses.
     * <p>
     * If it's determined that the strings are not addresses, we will do a string equality instead. This method was
     * created mainly to compare the contents of addresses in payloads which may be null, empty string, or a string
     * representing an IP address. The address may be in full notation or not.
     */
    static public boolean equals( String string1, String string2 )
    {
        boolean equal;

        if ( string1 != null )
        {
            try
            {
                InetAddress address1 = InetAddress.getByName( string1 );
                InetAddress address2 = InetAddress.getByName( string2 );

                equal = address1.equals( address2 );
            }
            catch ( UnknownHostException ex )
            {
                // They weren't IP addresses after all; do a string compare.
                equal = StringUtils.equals( string1, string2 );
            }
        }
        else
        {
            // string1 was null; so string2 better be.
            equal = ( string2 == null );
        }

        return equal;
    }
}
