package net.arin.tp.processor.response;

import org.apache.velocity.VelocityContext;

public interface ResponseContext
{
    void setup( VelocityContext context );

    String getTemplate();

    ResponseType getResponseType();

    boolean isTicketed();
}
