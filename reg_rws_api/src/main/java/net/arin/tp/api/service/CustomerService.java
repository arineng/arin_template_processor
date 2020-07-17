package net.arin.tp.api.service;

import net.arin.tp.api.payload.CustomerPayload;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * From a service perspective, the differences between customers and organizations are minimal. Customers can be made
 * private to keep the contact information hidden. Also, only customers can be used for simple reassignment.
 * Organizations cannot be used for that purpose. See {@link net.arin.tp.api.payload.CustomerPayload CustomerPayload}
 * for further details on the differences between a customer and an organization.<br/>
 * <br/>
 * Most service calls in this class will reference the comparable call for organizations. Response codes and behavior
 * will be identical except for the different payload object returned.<br/>
 * <br/>
 * Customers should only be created in relation to a network that will be reassigned. Therefore, the creation of a
 * customer is triggered off of a parent network. See the
 * {@link NetService#createRecipientCustomer(String, String, net.arin.tp.api.payload.CustomerPayload)} call under the
 * network services for more information.<br/>
 */
@Path( CustomerService.PATH )
@Consumes( Service.DEFAULT_MIMETYPE )
public interface CustomerService extends Service
{
    String PATH = "/customer";

    /**
     * Returns the customer details.<br/>
     *
     * @param customerHandle The customer handle of the customer to read
     * @param apiKey         The API Key that came in from the client
     * @return A CustomerPayload containing the details of the Customer
     */
    @GET
    @Path( "/{" + Parameters.CUSTOMER_HANDLE + "}" )
    CustomerPayload get( @PathParam( Parameters.CUSTOMER_HANDLE ) String customerHandle,
                         @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * Delete the customer.<br/>
     * <br/>
     * See the organization {@link OrgService#delete(String, String) delete} for a detailed description of response
     * codes and expected behavior.<br/>
     *
     * @param customerHandle The customer handle of the customer to remove
     * @param apiKey         The API Key that came in from the client
     * @return A CustomerPayload containing the details of the deleted Customer
     */
    @DELETE
    @Path( "/{" + Parameters.CUSTOMER_HANDLE + "}" )
    CustomerPayload delete( @PathParam( Parameters.CUSTOMER_HANDLE ) String customerHandle,
                            @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * Modify a customer.<br/>
     * <br/>
     * See the organization {@link OrgService#modify(String, String, OrgPayload) modify} for a detailed description of
     * response codes and expected behavior.<br/>
     *
     * @param customerHandle  The customer to modify
     * @param apiKey          The API key that came in from the client
     * @param customerPayload The CustomerPayload that contains the details of the modifications to be made
     * @return A CustomerPayload containing the details of the modified Customer
     */
    @PUT
    @Path( "/{" + Parameters.CUSTOMER_HANDLE + "}" )
    CustomerPayload modify( @PathParam( Parameters.CUSTOMER_HANDLE ) String customerHandle,
                            @QueryParam( Parameters.API_KEY ) String apiKey,
                            CustomerPayload customerPayload );
}
