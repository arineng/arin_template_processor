package net.arin.tp.api.service;

import net.arin.tp.api.payload.CustomerPayload;
import net.arin.tp.api.payload.DelegationPayload;
import net.arin.tp.api.payload.NetPayload;
import net.arin.tp.api.payload.OrgPayload;
import net.arin.tp.api.payload.PayloadList;
import net.arin.tp.api.payload.TicketedRequestPayload;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * These services provide interaction with your network resources at ARIN.<br/>
 * <br/>
 * Please note that these service calls will work with IPv4 and IPv6. The handle provided to any of these services can
 * represent either type.
 */
@Path( NetService.PATH )
@Consumes( Service.DEFAULT_MIMETYPE )
public interface NetService extends Service
{
    String PATH = "/net";

    /**
     * Finds the parent of the network represented by the start and end IP range.<br/>
     *
     * @param startIp Start IP address
     * @param endIp   End IP address
     * @param apiKey  The API key associated with the user performing the GET
     * @return A NetPayload containing the details of the parent Net
     */
    @GET
    @Path( "/parentNet/{" + Parameters.START_IP + "}/{" + Parameters.END_IP + "}" )
    NetPayload parentNet( @PathParam( Parameters.START_IP ) String startIp,
                          @PathParam( Parameters.END_IP ) String endIp,
                          @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * Finds the network details related to the start and end IP range.<br/>
     *
     * @param startIp Start IP address
     * @param endIp   End IP address
     * @param apiKey  The API key associated with the user performing the GET
     * @return A list of NetPayloads
     */
    @GET
    @Path( "/netsByIpRange/{" + Parameters.START_IP + "}/{" + Parameters.END_IP + "}" )
    PayloadList<NetPayload> netsByIpRange( @PathParam( Parameters.START_IP ) String startIp,
                                           @PathParam( Parameters.END_IP ) String endIp,
                                           @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * Finds the network details related to the start and end IP range. If multiple networks exist for the same IP
     * range, then the most specific network is returned.<br/>
     *
     * @param startIp Start IP address
     * @param endIp   End IP address
     * @param apiKey  The API key associated with the user performing the GET
     * @return A NetPayload containing the details of the most specific Net
     */
    @GET
    @Path( "/mostSpecificNet/{" + Parameters.START_IP + "}/{" + Parameters.END_IP + "}" )
    NetPayload mostSpecificNet( @PathParam( Parameters.START_IP ) String startIp,
                                @PathParam( Parameters.END_IP ) String endIp,
                                @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * Returns the details for a network.<br/>
     *
     * @param netHandle The netHandle of the network to get the details of
     * @param apiKey    The API key associated with the user performing the GET
     * @return A NetPayload containing the details of the Net
     */
    @GET
    @Path( "/{" + Parameters.NET_HANDLE + "}" )
    NetPayload get( @PathParam( Parameters.NET_HANDLE ) String netHandle,
                    @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * This call allows you to modify a network record in our database.<br/>
     *
     * @param netHandle The netHandle of the network to be modified
     * @param apiKey    The API key associated with the user performing the modification
     * @return A NetPayload containing the modified Net
     */
    @PUT
    @Path( "/{" + Parameters.NET_HANDLE + "}" )
    NetPayload modify( @PathParam( Parameters.NET_HANDLE ) String netHandle,
                       @QueryParam( Parameters.API_KEY ) String apiKey,
                       NetPayload netPayload );

    /**
     * Returns a list of delegation payloads for the specified network.<br/>
     *
     * @param netHandle The net handle of the network whose delegations should be retrieved
     * @param apiKey    The API key associated with the user performing the request
     * @return A list of DelegationPayloads for the Net
     */
    @GET
    @Path( "/{" + Parameters.NET_HANDLE + "}/delegations" )
    PayloadList<DelegationPayload> getDelegations( @PathParam( Parameters.NET_HANDLE ) String netHandle,
                                                   @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * <p>
     * This call will remove the network from our database. It is only applicable for reallocations (A) or
     * reassignments (S).
     * </p>
     * <p>
     * If there are no errors and the network being deleted has no children networks, this method will return a
     * {@link TicketedRequestPayload Ticketed Request Payload} with an embedded {@link NetPayload Net Payload} with
     * details of the deleted network. If the network being deleted has children, this method will return a
     * {@link TicketedRequestPayload Ticketed Request Payload} containing the details of the ticket created for the
     * request in a {@link net.arin.tp.api.payload.TicketPayload Ticket Payload}. <em><strong>If you plan to
     * reissue the space to another customer, please wait until the ticket successfully processes and is closed before
     * reissuing the space.</em></strong>
     * </p>
     * <p>
     * During the deletion of a Customer network obtained via a simple reassignment, only the network record will be
     * removed - the Customer record will remain on record. You will need to issue a separate customer delete request
     * to remove the customer record.
     * </p>
     * <p>
     * To return direct allocations (DA), please use Ask ARIN when logged in to ARIN Online. You will receive an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} if you attempt to use this REST service to return a
     * DA network.
     * </p>
     *
     * @param netHandle The handle of the network to be removed
     * @param apiKey    The API key associated with the user performing the delete
     * @return The resulting payload will contain either the network that was removed or ticket details for your request
     */
    @DELETE
    @Path( "/{" + Parameters.NET_HANDLE + "}" )
    TicketedRequestPayload delete( @PathParam( Parameters.NET_HANDLE ) String netHandle,
                                   @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * <p>
     * This call will remove the network from our database. It is only applicable for reallocations (A) or
     * reassignments (S). It differs from the delete call in that attachments can be sent using the NetPayload.
     * </p>
     * <p>
     * If there are no errors and the network being deleted has no children networks, this method will return a
     * {@link TicketedRequestPayload Ticketed Request Payload} with an embedded {@link NetPayload Net Payload} with
     * details of the deleted network. If the network being deleted has children, this method will return a
     * {@link TicketedRequestPayload Ticketed Request Payload} containing the details of the ticket created for the
     * request in a {@link net.arin.tp.api.payload.TicketPayload Ticket Payload}. <em><strong>If you plan to
     * reissue the space to another customer, please wait until the ticket successfully processes and is closed before
     * reissuing the space.</em></strong>
     * </p>
     * <p>
     * During the deletion of a Customer network obtained via a simple reassignment, only the network record will be
     * removed - the Customer record will remain on record. You will need to issue a separate customer delete request
     * to remove the customer record.</p>
     * <p>
     * To return direct allocations (DA), please use Ask ARIN when logged in to ARIN Online. You will receive an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} if you attempt to use this REST service to return a
     * DA network.
     * </p>
     *
     * @param netHandle  The handle of the network to be removed
     * @param apiKey     The API key associated with the user performing the delete
     * @param netPayload NetPayload representing the current details of the Net to be deleted, along with any attachments
     * @return The resulting payload will contain either the network that was removed or ticket details for your request
     */
    @PUT
    @Path( "/{" + Parameters.NET_HANDLE + "}/remove" )
    TicketedRequestPayload remove( @PathParam( Parameters.NET_HANDLE ) String netHandle,
                                   @QueryParam( Parameters.API_KEY ) String apiKey,
                                   NetPayload netPayload );

    /**
     * This method performs a reassignment for a network. It handles both detailed and simple reassignments.<br/>
     * <br/>
     * This method will take a NetPayload object and use it to reassign a portion of the parent network defined by
     * PARENT-NET-HANDLE. This service call should be used if the space is being given to a customer for their own use
     * and will not be reallocated or reassigned further.<br/>
     * <br/>
     * There are two types of reassignments that can take place: simple and detailed.<br/>
     * <br/>
     * A simple reassignment is used when allocating resources to a Customer Org (i.e. an Org without customer contact
     * information). You can create a Customer Org via
     * {@link NetService#createRecipientCustomer(String, String, net.arin.tp.api.payload.CustomerPayload)}. The
     * Customer Org to which resources are reassigned will use the POC records of the parent Org. A simple reassignment
     * is performed when a customerHandle is given in the NetPayload that is submitted to the service.<br/>
     * <br/>
     * A detailed reassignment is used when allocating resources to a Recipient Org (i.e. an Org with customer contact
     * information). A detailed reassignment is performed when an orgHandle is given in the NetPayload that is submitted
     * to the service.<br/>
     * <br/>
     * If there are no errors, this method will return a TicketedRequestPayload. If the reassignment can be
     * auto-processed, this TicketedRequestPayload will have an embedded NetPayload with the details of the
     * reassignment. A reassignment can be auto-processed if:<br/>
     * <br/>
     * <ol>
     * <li>It is not auto-rejected; and</li>
     * <li>It does not require manual intervention</li>
     * </ol>
     * <br/>
     * If the request cannot be auto-processed, the TicketedRequestPayload returned will have an embedded TicketPayload
     * containing the details of the ticket created for the request. A reassignment requires manual intervention if:<br/>
     * <br/>
     * <ol>
     * <li>The V4 simple reassignment request is for a /20 or larger.</li>
     * <li>The recipient of the V6 reassignment already has V6 space.</li>
     * </ol>
     * <br/>
     * If a reassignment can not be processed, the reason for rejection will be returned in an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. This method will return a 409 Conflict if the
     * network block specified in the payload isn't a part of the PARENT-NET-HANDLE from the URL. It will return a 403
     * Forbidden if the PARENT-NET-HANDLE isn't associated with the API Key specified or if the block specified by the
     * payload cannot be reassigned.<br/>
     * <br/>
     * A simple reassignment is rejected if:<br/>
     * <br/>
     * <ol>
     * <li>The customer handle provided does not exist.</li>
     * <li>POCs are associated to the Simple Reassign Network.</li>
     * <li>Net name contains characters that are not letters, numbers, hyphens or spaces.</li>
     * <li>Parent network does not exist.</li>
     * <li>Parent network has a status other than active.</li>
     * <li>Parent network does not have a network of type DA or A.</li>
     * <li>IP addresses within the range extend beyond that of the Parent network.</li>
     * <li>IP addresses within the range overlap with existing reservations or registrations that have the same parent.</li>
     * <li>API Key used is not associated with a web user who is linked to an Admin or Tech POC for the parent network's
     * org record or to a Tech poc for the parent network.</li>
     * <li>API Key is not active.</li>
     * <li>The reassignment request is smaller than /64 for IPv6 space.</li>
     * </ol>
     * <br/>
     * A detailed reassign is rejected if:<br/>
     * <br/>
     * <ol>
     * <li>The org handle provided does not exist.</li>
     * <li>The organization provided does not have an Admin and at least one Tech POC and at least one Abuse POC.</li>
     * <li>POCs are associated to the Detailed Reassign Network. POC relationships are not allowed to be added
     * during network creation.</li>
     * <li>Net name contains characters that are not letters numbers hyphens or spaces.</li>
     * <li>Parent network does not exist.</li>
     * <li>Parent network has a status other than active.</li>
     * <li>Parent network does not have a network of type DA or A.</li>
     * <li>IP addresses within the range extend beyond that of the Parent network.</li>
     * <li>IP addresses within the range overlap with existing reservations or registrations that have the same parent.</li>
     * <li>API Key used is not associated with a web user who is linked to an Admin or Tech POC for the parent network's
     * org record or to a Tech poc for the parent network.</li>
     * <li>API Key is not active. </li>
     * </ol>
     * <br/>
     *
     * @param netHandle  The netHandle of the network to be removed
     * @param apiKey     The API key associated with the user performing the delete
     * @param netPayload A JAXB annotated object representing the details of the requested reassignment
     * @return TicketedRequestPayload containing either a NetworkPayload containing details of the successfully
     * reassigned network or a TicketPayload if the request requires manual intervention; an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} may also be returned if there was an error
     * processing the request
     */
    @PUT
    @Path( "/{" + Parameters.PARENT_NET_HANDLE + "}/reassign" )
    TicketedRequestPayload reassign( @PathParam( Parameters.PARENT_NET_HANDLE ) String netHandle,
                                     @QueryParam( Parameters.API_KEY ) String apiKey,
                                     NetPayload netPayload );

    /**
     * This service method performs a reallocation of a network. This service call should be used if you're reallocating
     * space to allow your customer to reassign or reallocate it further. If there are no errors, this method will
     * return a TicketedRequestPayload.<br/>
     * <br/>
     * If the reallocation can be auto-processed, this TicketedRequestPayload will have an embedded NetPayload with the
     * details of the reallocation. A reallocation can be auto-processed if:<br/>
     * <br/>
     * <ol>
     * <li>It is not auto rejected; and</li>
     * <li>It does not require manual intervention</li>
     * </ol>
     * <br/>
     * If the request cannot be auto-processed, the TicketedRequestPayload returned will have an embedded
     * TicketPayload containing the details of the ticket created for the request. A reallocation requires manual
     * intervention by an individual within the ARIN organization if:<br/>
     * <br/>
     * <ol>
     * <li>The V4 reallocation requested is larger than a /20; or</li>
     * <li>The V6 reallocation is issued to an organization that already has resources</li>
     * </ol>
     * <br/>
     * If a reallocation can not be processed, the reason for rejection will be returned in an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. This method will return a 409 Conflict if the
     * network block specified in the payload isn't a part of the PARENT-NET-HANDLE from the URL. It will return a 403
     * Forbidden if the PARENT-NET-HANDLE isn't associated with the API Key specified or if the block specified by the
     * payload cannot be reallocated. Reallocations will be auto-rejected if:<br/>
     * <br/>
     * <ol>
     * <li>The org handle provided does not exist.</li>
     * <li>The organization provided does not have an Admin and at least one Tech POC and at least one Abuse POC.</li>
     * <li>POCs are associated to the Reallocated Network. POC relationships are not allowed to be added
     * during network creation.</li>
     * <li>Net name contains characters that are not letters numbers hyphens or spaces.</li>
     * <li>Parent network does not exist.</li>
     * <li>Parent network has a status other than active.</li>
     * <li>Parent network does not have a network of type DA or A.</li>
     * <li>IP addresses within the range extend beyond that of the Parent network.</li>
     * <li>IP addresses within the range overlap with existing reservations or registrations that have the same parent.</li>
     * <li>API Key used is not associated with a web user who is linked to an Admin or Tech POC for the parent network's
     * org record or to a Tech poc for the parent network.</li>
     * <li>API Key is not active.</li>
     * </ol>
     * <br/>
     *
     * @param netHandle  The netHandle of the network to be removed
     * @param apiKey     The API key associated with the user performing the delete
     * @param netPayload A JAXB annotated object representing the details of the requested reallocation
     * @return TicketedRequestPayload containing either a NetworkPayload containing details of the successfully
     * reallocated network or a TicketPayload if the request requires manual intervention; an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} may also be returned if there was an error processing
     * the request
     */
    @PUT
    @Path( "/{" + Parameters.PARENT_NET_HANDLE + "}/reallocate" )
    TicketedRequestPayload reallocate( @PathParam( Parameters.PARENT_NET_HANDLE ) String netHandle,
                                       @QueryParam( Parameters.API_KEY ) String apiKey,
                                       NetPayload netPayload );

    /**
     * This method should be used to create downstream customer organizations without customer contact information to
     * which you will reassign network address space. If you are wanting to request resources directly from ARIN, use
     * the organization {@link OrgService#create(String, OrgPayload) create}.<br/>
     * <br/>
     * This call will create a customer organization record based on the payload provided. Once the customer
     * organization has been created, a new payload will be constructed and returned showing what was committed to the
     * database. This returned payload will also contain the new Customer handle that was created.<br/>
     * <br/>
     * This call must include the PARENT-NET-HANDLE of the network from which you will be reassigning or reallocating
     * space.<br/>
     * <br/>
     * In order for this method to succeed, one of the following three conditions must be met:<br/>
     * <br/>
     * <ol>
     * <li>The API Key must belong to a Web User that is associated with the Tech POC of the Network specified by PARENT-NET-HANDLE.</li>
     * <li>The API Key must belong to a Web User that is associated with the Tech POC of the Org to which the network indicated by PARENT-NET-HANDLE is allocated.</li>
     * <li>The API Key must belong to a Web User that is associated with the Admin POC of the Org to which the network indicated by PARENT-NET-HANDLE is allocated.</li>
     * </ol>
     *
     * @param parentNetHandle The parent net from which space will be allocated to this Customer
     * @param apiKey          The API key that came in from the client
     * @param customerPayload A JAXB annotated object representing the Org attributes
     * @return A CustomerPayload containing the details of the customer the was just created
     */
    @POST
    @Path( "/{" + Parameters.PARENT_NET_HANDLE + "}/customer" )
    CustomerPayload createRecipientCustomer( @PathParam( Parameters.PARENT_NET_HANDLE ) String parentNetHandle,
                                             @QueryParam( Parameters.API_KEY ) String apiKey,
                                             CustomerPayload customerPayload );
}
