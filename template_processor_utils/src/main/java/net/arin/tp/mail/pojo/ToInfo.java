package net.arin.tp.mail.pojo;

import java.util.Collection;
import java.util.Collections;

public class ToInfo
{
    public final Collection<EmailAddress> toAddresses;
    public final Collection<EmailAddress> ccAddresses;
    public final Collection<EmailAddress> bccAddresses;

    public ToInfo( Collection<EmailAddress> toAddresses, Collection<EmailAddress> ccAddresses, Collection<EmailAddress> bccAddresses )
    {
        this.toAddresses = Collections.unmodifiableCollection( toAddresses );
        if ( ccAddresses != null )
        {
            this.ccAddresses = Collections.unmodifiableCollection( ccAddresses );
        }
        else
        {
            this.ccAddresses = Collections.unmodifiableCollection( Collections.emptyList() );
        }
        if ( bccAddresses != null )
        {
            this.bccAddresses = Collections.unmodifiableCollection( bccAddresses );
        }
        else
        {
            this.bccAddresses = Collections.unmodifiableCollection( Collections.emptyList() );
        }
    }

    public ToInfo( Collection<EmailAddress> toAddresses )
    {
        this( toAddresses, Collections.emptyList(), Collections.emptyList() );
    }

    public ToInfo( EmailAddress toAddress )
    {
        this( Collections.singletonList( toAddress ),
                Collections.emptyList(),
                Collections.emptyList() );
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "To:" );
        builder.append( toAddresses );
        if ( !ccAddresses.isEmpty() )
        {
            builder.append( "\tCc:" );
            builder.append( ccAddresses );
        }
        if ( !bccAddresses.isEmpty() )
        {
            builder.append( "\tBcc:" );
            builder.append( bccAddresses );
        }
        return builder.toString();
    }
}
