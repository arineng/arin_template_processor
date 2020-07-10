package net.arin.tp.mail.pojo;

import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EmailAddress implements Serializable
{
    private String email;
    private String displayName;

    /**
     * Empty constructor and getters/setters needed for JSON conversion. Otherwise, use other provided constructors to
     * avoid setters.
     */
    public EmailAddress()
    {
    }

    public EmailAddress( String email, String displayName )
    {
        this.email = email;
        this.displayName = displayName;
    }

    public EmailAddress( String email )
    {
        this.email = email;
        this.displayName = "";
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    public InternetAddress toInternetAddress()
    {
        try
        {
            return new InternetAddress( this.email, this.displayName );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new RuntimeException( e );
        }
    }

    public static List<EmailAddress> emailAddresses( Collection<String> stringEmailAddresses )
    {
        List<EmailAddress> emailAddresses = new ArrayList<>();
        for ( String stringEmailAddress : stringEmailAddresses )
        {
            emailAddresses.add( new EmailAddress( stringEmailAddress ) );
        }

        return Collections.unmodifiableList( emailAddresses );
    }

    public static List<EmailAddress> emailAddresses( String stringEmailAddress )
    {
        return emailAddresses( Collections.singletonList( stringEmailAddress ) );
    }

    @Override
    public String toString()
    {
        return toInternetAddress().toString();
    }

    @Override
    public int hashCode()
    {
        return toInternetAddress().hashCode();
    }

    @Override
    public boolean equals( Object other )
    {
        return ( other instanceof EmailAddress ) &&
                toInternetAddress().equals( ( ( EmailAddress ) other ).toInternetAddress() );
    }
}
