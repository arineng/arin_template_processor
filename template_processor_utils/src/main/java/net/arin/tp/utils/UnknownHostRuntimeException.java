package net.arin.tp.utils;

import java.net.UnknownHostException;

public class UnknownHostRuntimeException extends RuntimeException
{
    public UnknownHostRuntimeException( String message )
    {
        super( message );
    }

    public UnknownHostRuntimeException( String description, UnknownHostException unknownHostException )
    {
        super( description, unknownHostException );
    }

    public UnknownHostRuntimeException( UnknownHostException unknownHostException )
    {
        super( unknownHostException );
    }
}
