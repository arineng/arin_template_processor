package net.arin.tp.mail.pojo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FromInfo
{
    public final EmailAddress fromAddress;
    public final Collection<EmailAddress> replyToAddresses;

    public FromInfo( EmailAddress fromAddress, List<EmailAddress> replyToAddresses )
    {
        this.fromAddress = fromAddress;
        this.replyToAddresses = Collections.unmodifiableCollection( replyToAddresses );
    }

    public FromInfo( EmailAddress fromAddress, EmailAddress replyToAddress )
    {
        this( fromAddress, Collections.singletonList( replyToAddress ) );
    }

    public FromInfo( EmailAddress fromAddress )
    {
        this( fromAddress, Collections.emptyList() );
    }

    public FromInfo( String fromAddress, List<String> replyToAddresses )
    {
        this( new EmailAddress( fromAddress ), EmailAddress.emailAddresses( replyToAddresses ) );
    }

    public FromInfo( String fromAddress, String replyToAddress )
    {
        this( new EmailAddress( fromAddress ), EmailAddress.emailAddresses( replyToAddress ) );
    }

    public FromInfo( String fromAddress )
    {
        this( new EmailAddress( fromAddress ), Collections.emptyList() );
    }
}
