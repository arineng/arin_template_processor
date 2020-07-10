package net.arin.tp.processor.response;

public abstract class AbstractResponseContext implements ResponseContext
{
    public boolean isTicketed()
    {
        return false;
    }
}
