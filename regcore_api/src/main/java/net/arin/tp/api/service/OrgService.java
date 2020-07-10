package net.arin.tp.api.service;

import net.arin.tp.api.payload.OrgPayload;
import net.arin.tp.api.payload.PocLinkPayload;
import net.arin.tp.api.payload.TicketPayload;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * The ORG service provides interaction with your Organization (ORG) records at ARIN.<br/>
 */
@Path( OrgService.PATH )
@Consumes( Service.DEFAULT_MIMETYPE )
public interface OrgService extends Service
{
    String PATH = "/org";

    /**
     * This GET method fetches the details of an ORG record. The ORGHANDLE is the identifier of the ORG record to be
     * fetched.<br/>
     * <br/>
     * If no ORG exists for the ORGHANDLE specified in the request, an HTTP 404 (not found) response containing an
     * E_OBJECT_NOT_FOUND {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned.<br/>
     * <br/>
     * The APIKEY used in the request must belong to an ARIN Online user that is linked to a POC that has authorization
     * to view the Organization record. An attempt to fetch an ORG for which such a link does not exist will result in
     * an HTTP 401 (unauthorized) response containing an E_AUTHENTICATION
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. Invalid or inactivated APIKEYs will result in an HTTP
     * 400 (bad request) response.<br/>
     *
     * @param orgHandle The org handle of the organization to read
     * @param apiKey    The API Key that came in from the client
     * @return An OrgPayload containing the details of the Org
     */
    @GET
    @Path( "/{" + Parameters.ORG_HANDLE + "}" )
    OrgPayload get( @PathParam( Parameters.ORG_HANDLE ) String orgHandle,
                    @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * This POST method creates an ORG record based on its accompanying payload. If successful, the response contains
     * an ORG payload containing the details of the ORG record, including the new ORG's ORG Handle (also known as an
     * ORG ID) which is the identifer used to locate the ORG for future transactions.<br/>
     * <br/>
     * If a validation error occurs, the ORG is not created and an HTTP 400 (bad request) response containing an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned along with an error code and message
     * explaining the error.<br/>
     * <br/>
     * If the request erroneously contains an ORGHANDLE in the URL, an HTTP 405 (method not allowed) response is
     * returned. The POST method does not accept an ORGHANDLE, because a new one is generated upon success of the
     * request and is included in the ORG payload.<br/>
     * <br/>
     * This method is typically used to create an ORG record for an organization that wants to request a Direct
     * Allocation or Direct Assignment from ARIN. It should NOT be used to create a downstream organization that an
     * upstream ISP wants to reassign or reallocate networks to.<br/>
     * <br/>
     * Note that in most cases, the submitter will want to ensure that a POC, linked to the submitter's ARIN Online user
     * account (either thru ARIN Online, or by creating the POC with makeLink=true), is included as an Admin or Tech POC
     * associated with the ORG being created in this request.  Failure to do so will result in creation of the ORG, but
     * the submitter will not have the authorization to execute subsequent GET, MODIFY, or DELETE actions on the ORG due
     * to insufficient authorization.<br/>
     *
     * @param apiKey     The API key that came in from the client
     * @param orgPayload A JAXB annotated object representing the Org attributes
     * @return A TicketPayload containing the ticket details for the org creation ticket
     */
    @POST
    TicketPayload create( @QueryParam( Parameters.API_KEY ) String apiKey,
                          OrgPayload orgPayload );

    /**
     * This DELETE method removes an ORG record. The ORGHANDLE is the identifier of the ORG record to be removed. If
     * successful, the response contains an ORG payload representing the state of the ORG record just before removal.<br/>
     * <br/>
     * If the ORG is still associated with number resources (either NETs or ASNs), the ORG cannot be removed until those
     * ASN or NET associations are first removed (by returning or removing the number resources as appropriate). In
     * such cases, an HTTP 409 (conflict) response containIng an E_NOT_REMOVABLE
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned.<br/>
     * <br/>
     * The APIKEY used in the request must belong to an ARIN Online user that is linked to a POC that has authorization
     * to remove the Organization record. An attempt to remove an ORG for which such a link does not exist will result
     * in an HTTP 401 (unauthorized) response containing an E_AUTHENTICATION
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. Invalid or inactivated APIKEYs will result in an HTTP
     * 400 (bad request) response.<br/>
     *
     * @param orgHandle The organization to delete
     * @param apiKey    The API key that came in from the client
     * @return An organization payload representing the organization removed from the database
     */
    @DELETE
    @Path( "/{" + Parameters.ORG_HANDLE + "}" )
    OrgPayload delete( @PathParam( Parameters.ORG_HANDLE ) String orgHandle,
                       @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * This PUT method modifies an ORG record. The ORGHANDLE is the identifier of the ORG record to be modified. If
     * successful, the response contains a payload representing the updated ORG record.<br/>
     * <br/>
     * If a validation error occurs or if the request attempts to modify immutable fields, the ORG is not modified and
     * an HTTP 400 (bad request) response containing an {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is
     * returned along with an error code and message explaining the error.
     * (See {@link net.arin.tp.api.payload.OrgPayload OrgPayload} for information on immutable fields.)<br/>
     * <br/>
     * The APIKEY used in the request must belong to an ARIN Online user that is linked to a POC that has authorization
     * to modify the Organization record. An attempt to modify an ORG for which such a link does not exist will result
     * in an HTTP 401 (unauthorized) response containing an E_AUTHENTICATION
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. Invalid or inactivated APIKEYs will result in an HTTP
     * 400 (bad request) response.<br/>
     * <br/>
     * If any POC information is provided in the ORG payload of the PUT request, all current POCs associated with the
     * ORG will be removed, and replaced with those provided in the request. To add or remove individual POCs to/from an
     * ORG, use the ADD or DELETE POC service methods.<br/>
     *
     * @param orgHandle  The handle of the organization to modify
     * @param apiKey     The API key for the user making the request
     * @param orgPayload The payload containing the modifications to be made
     * @return An organization payload representing the modifications committed to the database
     */
    @PUT
    @Path( "/{" + Parameters.ORG_HANDLE + "}" )
    OrgPayload modify( @PathParam( Parameters.ORG_HANDLE ) String orgHandle,
                       @QueryParam( Parameters.API_KEY ) String apiKey,
                       OrgPayload orgPayload );

    /**
     * <p>
     * Similar to how a phone can be removed from a POC
     * ({@link net.arin.tp.api.service.PocService#deletePhone(String, String, String, net.arin.tp.api.payload.PhonePayload.PhoneTypePayload)}),
     * this call allows you to optionally specify both the POC handle and the type to remove a POC from an organization,
     * but at least one of those must be specified.<br/>
     * <br/>
     * If you don't specify the type matrix parameter, the POC corresponding to POC-HANDLE will be removed from all the
     * types. If it matches a single record, it will remove that one record; but if the POC is specified as both an
     * ADMIN and TECH record, it will be removed for both.<br/>
     * <br/>
     * Regardless of the combination you use to remove POC links to organizations, the resulting organization record
     * must still pass validation before the changes are final. If you were to try and delete all POC tech roles, it
     * will fail with a validation error and your changes won't be flushed to the database. Instead you'll receive a
     * "400" status code and {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} denoting a validation error
     * occurred.<br/>
     * <br/>
     * This service will return a PayloadList of {@link PocLinkPayload PocLinkPayload} objects that were removed from
     * the POC record. If no records were removed, you will still receive a "200 OK" and an empty PayloadList record.
     * Due to the querying capability and open ended nature of these queries, returning a 404 didn't make sense. Also,
     * since this is a subservice off of the main organization service, a 404 will be used to denote that the ORG-HANDLE
     * provided does not exist.<br/>
     *
     * @param orgHandle The organization to remove the POC from
     * @param pocHandle The POC to remove
     * @param apiKey    The API key for the user making the request
     * @return An organization payload representing the modifications committed to the database
     */
    @DELETE
    @Path( "/{" + Parameters.ORG_HANDLE + "}/poc/{" + Parameters.POC_HANDLE + "}" )
    OrgPayload removePoc( @PathParam( Parameters.ORG_HANDLE ) String orgHandle,
                          @PathParam( Parameters.POC_HANDLE ) String pocHandle,
                          @MatrixParam( Parameters.POC_FUNCTION ) PocLinkPayload.Function pocFunction,
                          @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * This PUT method associates a POC with an ORG. The ORGHANDLE is the identifier of the ORG record to which the new
     * POCHANDLE will be associated. The POCFUNCTION is the function (Admin, Tech, Abuse, etc.) that the POC will
     * perform on behalf of the ORG. ORGHANDLE, POCHANDLE, and POCFUNCTION are all provided in the URL. If successful,
     * the response contains a payload representing the ORG, which will in turn contain a POCs collection that lists all
     * of the ORG's POCs by function, including the one added by submission of this method.<br/>
     * <br/>
     * Both the ORG and the POC being associated with the ORG must exist prior to submission of this PUT request. If
     * either do not exist, an HTTP 404 (not found) response containing an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned along with an error code and message
     * explaining the error.<br/>
     * <br/>
     * This method is convenient when the user wants to associate a POC with an ORG for a particular FUNCTION but does
     * not want to respecify the entire ORG record via a ORG MODIFY (PUT) request. For all POC functions except for
     * Admin, this method does not destroy existing POC associations, rather it simply adds an additional association
     * for the specified FUNCTION.<br/>
     * <br/>
     * Special note for Admin POC: If this method is used to associate a POC with the ORG for the Admin FUNCTION, the
     * existing Admin POC will be removed and the POC specified in this request will become the new Admin POC.<br/>
     * <br/>
     * If a validation error occurs, the POC is not associated with the ORG and an HTTP 400 (bad request) response
     * containing an {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned along with an error code and
     * message explaining the error.<br/>
     * <br/>
     * The APIKEY used in the request must belong to an ARIN Online user that is linked to a POC that has authorization
     * to modify the Organization record. An attempt to modify an ORG for which such a link does not exist will result
     * in an HTTP 401 (unauthorized) response containing an E_AUTHENTICATION
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. Invalid or inactivated APIKEYs will result in an HTTP
     * 400 (bad request) response.<br/>
     *
     * @param orgHandle The org to modify
     * @param pocHandle The poc to remove
     * @param apiKey    The API key that came in from the client
     * @return An organization payload representing the modifications committed to the database
     */
    @PUT
    @Path( "/{" + Parameters.ORG_HANDLE + "}/poc/{" + Parameters.POC_HANDLE + "}" )
    OrgPayload addPoc( @PathParam( Parameters.ORG_HANDLE ) String orgHandle,
                       @PathParam( Parameters.POC_HANDLE ) String pocHandle,
                       @MatrixParam( Parameters.POC_FUNCTION ) PocLinkPayload.Function pocFunction,
                       @QueryParam( Parameters.API_KEY ) String apiKey );
}
