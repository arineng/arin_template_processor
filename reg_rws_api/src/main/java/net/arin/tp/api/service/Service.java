package net.arin.tp.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import java.io.Serializable;

/**
 * Sets the default consumes/produces for all services.
 */
@Produces( Service.DEFAULT_MIMETYPE )
@Consumes( Service.DEFAULT_MIMETYPE )
public interface Service extends Serializable
{
    /**
     * The default mime-type for our services.
     */
    String DEFAULT_MIMETYPE = "application/xml";
}
