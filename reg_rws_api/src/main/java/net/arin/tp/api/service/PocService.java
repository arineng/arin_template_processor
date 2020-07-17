package net.arin.tp.api.service;

import net.arin.tp.api.payload.PayloadList;
import net.arin.tp.api.payload.PhonePayload;
import net.arin.tp.api.payload.PhoneTypePayload;
import net.arin.tp.api.payload.PocPayload;

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
 * The POC service provides interaction with your point-of-contact (POC) records at ARIN.<br/>
 */
@Path( PocService.PATH )
@Consumes( Service.DEFAULT_MIMETYPE )
public interface PocService extends Service
{
    String PATH = "/poc";

    /**
     * This GET method fetches the details of a POC record. The POCHANDLE is the identifier of the POC record to be
     * fetched.<br/>
     * <br/>
     * If no POC exists for the POCHANDLE specified in the request, an HTTP 404 (not found) response containing an
     * E_OBJECT_NOT_FOUND {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned.<br/>
     * <br/>
     * The APIKEY used in the request must belong to an ARIN Online user that is linked to the POC. An attempt to fetch
     * a POC for which such a link does not exist will result in an HTTP 401 (unauthorized) response containing an
     * E_AUTHENTICATION {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. Invalid or inactivated APIKEYs will
     * result in an HTTP 400 (bad request) response.
     *
     * @param pocHandle The handle of the POC to retrieve
     * @param apiKey    The requesting user's API key
     * @return A pocPayload detailing the poc information requested
     */
    @GET
    @Path( "/{" + Parameters.POC_HANDLE + "}" )
    PocPayload get( @PathParam( Parameters.POC_HANDLE ) String pocHandle,
                    @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * This POST method creates a POC record based on its accompanying payload. If successful, the response contains
     * a POC payload containing the details of the POC record, including the new POC's handle, which is the identifier
     * used to locate the POC for future transactions.<br/>
     * <br/>
     * If a validation error occurs, the POC is not created and an HTTP 400 (bad request) response containing an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned along with an error code and message
     * explaining the error.<br/>
     * <br/>
     * If the request erroneously contains a POCHANDLE in the URL, an HTTP 405 (method not allowed) response is
     * returned. The POST method does not accept a POCHANDLE, because a new one is generated upon success of the request
     * and is included in the POC payload.<br/>
     * <br/>
     * If MAKELINK is specified as <em>true</em>, the system will create a link between the ARIN Online user account
     * identified by the APIKEY in the request and the newly created POC. If MAKELINK is omitted, or specified as
     * <em>false</em>, then no link will be created between the submitter and the new POC. Specifying makeLink=true is
     * useful when the submitter wants to retain control over the POC record for later update or removal. Note that for
     * the "person" contactType, only one link to an ARIN Online user account is permitted; for the "role" contactType,
     * multiple ARIN Online user accounts can link to the POC. (See {@link net.arin.tp.api.payload.PocPayload PocPayload}
     * for information on specifying contactType.)
     *
     * @param apiKey         The API key that came in from the client
     * @param autoLinkToUser Boolean indicating whether or not the newly created POC should be linked to the user
     *                       associated to the API key
     * @param pocPayload     A JAXB annotated object representing the POC attributes
     * @return A POC payload containing the newly created POC
     */
    @POST
    PocPayload create( @QueryParam( Parameters.API_KEY ) String apiKey,
                       @MatrixParam( Parameters.MAKE_LINK ) boolean autoLinkToUser,
                       PocPayload pocPayload );

    /**
     * This DELETE method removes a POC record. The POCHANDLE is the identifier of the POC record to be removed. If
     * successful, the response contains a POC payload representing the state of the POC record just before removal.<br/>
     * <br/>
     * A POC that is associated with an ORG record (example: as an Abuse POC), or with number resources (NET or ASN),
     * cannot be removed until those associations are first removed (by modifying the ORG record or number resource
     * record to remove its association with the POC). In such cases, an HTTP 409 (conflict) response containing an
     * E_NOT_REMOVABLE {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned.<br/>
     * <br/>
     * The APIKEY used in the request must belong to an ARIN Online user that is linked to the POC. An attempt to remove
     * a POC for which such a link does not exist will result in an HTTP 401 (unauthorized) response containing an
     * E_AUTHENTICATION {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. Invalid or inactivated APIKEYs will
     * result in an HTTP 400 (bad request) response.<br/>
     *
     * @param pocHandle The pocHandle from the RESTful path
     * @param apiKey    The API key that came in from the client
     * @return A POC payload containing the removed POC
     */
    @DELETE
    @Path( "/{" + Parameters.POC_HANDLE + "}" )
    PocPayload delete( @PathParam( Parameters.POC_HANDLE ) String pocHandle,
                       @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * This PUT method modifies a POC record. The POCHANDLE is the identifier of the POC record to be modified. If
     * successful, the response contains a payload representing the updated POC record.<br/>
     * <br/>
     * If a validation error occurs or if the request attempts to modify immutable fields, the POC is not modified and
     * an HTTP 400 (bad request) response containing an {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is
     * returned along with an error code and message explaining the error.
     * (See {@link net.arin.tp.api.payload.PocPayload PocPayload} for information on immutable fields.)<br/>
     * <br/>
     * The APIKEY used in the request must belong to an ARIN Online user that is linked to the POC. An attempt to modify
     * a POC for which such a link does not exist will result in an HTTP 401 (unauthorized) response containing an
     * E_AUTHENTICATION {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. Invalid or inactivated APIKEYs will
     * result in an HTTP 400 (bad request) response.<br/>
     * <br/>
     * If contact phone or email address information is provided in the POC payload of the PUT request, all current
     * phones or email addresses associated with the POC will be removed, and replaced with those provided in the
     * request. To add or remove individual phones or email addresses to/from a POC, use the ADD or DELETE PHONE or ADD
     * or DELETE EMAIL service methods. <br/>
     *
     * @param pocHandle  The pocHandle from the RESTful path
     * @param apiKey     The API key that came in from the client
     * @param pocPayload A JAXB annotated object representing the changes to the POC
     * @return A POC payload containing the new state of the POC
     */
    @PUT
    @Path( "/{" + Parameters.POC_HANDLE + "}" )
    PocPayload modify( @PathParam( Parameters.POC_HANDLE ) String pocHandle,
                       @QueryParam( Parameters.API_KEY ) String apiKey,
                       PocPayload pocPayload );

    /**
     * This PUT method associates a phone with a POC and identifies the PHONE type. The POCHANDLE is the identifier of
     * the POC record to which the new PHONE record should be added. If successful, the response contains a payload
     * representing the PHONE record added to the POC.<br/>
     * <br/>
     * This method is convenient when the user wants to add a PHONE to a POC but does not want to respecify the entire
     * POC record via a POC MODIFY (PUT) request. This method does not destroy existing PHONE records, rather it simply
     * adds an additional PHONE.<br/>
     * <br/>
     * If a validation error occurs, the PHONE/TYPE combination is not added and an HTTP 400 (bad request) response
     * containing an {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned along with an error code and
     * message explaining the error.<br/>
     * <br/>
     * The APIKEY used in the request must belong to an ARIN Online user that is linked to the POC. An attempt to
     * modify a POC (by adding a PHONE) for which such a link does not exist will result in an HTTP 401 (unauthorized)
     * response containing an E_AUTHENTICATION {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. Invalid or
     * inactivated APIKEYs will result in an HTTP 400 (bad request) response.<br/>
     *
     * @param pocHandle    The pocHandle from the RESTful path
     * @param apiKey       The API key that came in from the client
     * @param phonePayload A JAXB annotated object representing the changes to the POC
     * @return The phone payload that was added to the POC
     */
    @PUT
    @Path( "/{" + Parameters.POC_HANDLE + "}/phone" )
    PhonePayload addPhone( @PathParam( Parameters.POC_HANDLE ) String pocHandle,
                           @QueryParam( Parameters.API_KEY ) String apiKey,
                           PhonePayload phonePayload );

    /**
     * This DELETE method removes a phone from a POC. The POCHANDLE is the identifier of the POC record from which the
     * PHONE will be removed; the NUMBER is used to identify the PHONE NUMBER to be removed. The TYPE is used to
     * identify the PHONE TYPE to be removed. If successful, the response contains a PayloadList collection of
     * {@link net.arin.tp.api.payload.PhonePayload PhonePayloads} representing PHONE records that were removed from the
     * POC.<br/>
     * <br/>
     * This method is different from the most service calls in the POC service. It is convenient in several cases:<br/>
     * - when the user wants to remove an individual PHONE number associated with a particular phone TYPE for a POC<br/>
     * - when the user wants to remove a PHONE number that is used as more than one phone TYPE for a POC<br/>
     * - when the user wants to remove all the PHONE numbers for a particular phone TYPE for a POC<br/>
     * <br/>
     * This method is useful because it permits removal of a PHONE without having to respecify the entire POC structure
     * or entire PHONE list.<br/>
     * <br/>
     * To remove an individual phone NUMBER associated with a particular phone TYPE, the user must provide the PHONE in
     * the URL and a PhoneTypePayload specifying the Phone TYPE of the phone to be removed from the POC. If successful,
     * the PayloadList will contain the single PHONE record removed.<br/>
     * <br/>
     * If the user does not specify a NUMBER in the URL, then the phone TYPE must be specified in the URL, and all phone
     * NUMBERs of that TYPE will be removed from the POC.  If successful, the PayloadList will contain all PHONE records
     * removed, of the TYPE specified in the URL. Note that the NUMBER must match the value in the database exactly,
     * including all "+" and "-" characters.<br/>
     * <br/>
     * If the user does not specify a TYPE in the URL, then the phone NUMBER must be specified in the URL, and that
     * phone NUMBER will be removed from the POC for all TYPEs. If it matches a single record, it will remove that one
     * record, but (for example) if the NUMBER exists as both an O (office) and F (fax) TYPE, then both will be
     * removed from the POC. If successful, the PayloadList will contain all PHONE records removed for the NUMBER
     * specified in the URL. Note that the phone types are detailed in the
     * {@link net.arin.tp.api.payload.PhoneTypePayload PhoneTypePayload}.<br/>
     * <br/>
     * Regardless of the combination of NUMBER and TYPE used to remove phone numbers, the resulting POC record must
     * still pass validation before the changes are accepted and the service returns a success (HTTP 200) response. For
     * example, if the submission attempts to delete all office phone numbers for a POC, it fails (HTTP 400) with a
     * validation error (because every POC must have at least one OFFICE phone).<br/>
     * <br>
     * If a validation error occurs, the PHONE is not removed and an HTTP 400 (bad request) response containing an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned along with an error code and message
     * explaining the error.<br/>
     * <br/>
     * The APIKEY used in the request must belong to an ARIN Online user that is linked to the POC.  An attempt to
     * modify a POC (by removing a PHONE) for which such a link does not exist will result in an HTTP 401 (unauthorized)
     * response containing an E_AUTHENTICATION {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. Invalid or
     * inactivated APIKEYs will result in an HTTP 400 (bad request) response.<br/>
     * <br/>
     * Special note regarding this service method: If no records match the phone NUMBER and TYPE combination provided in
     * the request, the submitter still receives an HTTP 200 (success) message, accompanied by an empty PayloadList
     * record indicating that no records were removed, rather than receiving an HTTP 404 (not found), since no records
     * matched the NUMBER and/or TYPE. The use of the HTTP 404 (not found) for this method is reserved to denote that
     * the POCHANDLE in the request does not exist.<br/>
     *
     * @param pocHandle The pocHandle from the RESTful path
     * @param apiKey    The API key that came in from the client
     * @return The list of phone payloads that were removed from the record
     */
    @DELETE
    @Path( "/{" + Parameters.POC_HANDLE + "}/phone/{number:.*}" )
    PayloadList<PhonePayload> deletePhone( @PathParam( Parameters.POC_HANDLE ) String pocHandle,
                                           @PathParam( "number" ) String number,
                                           @QueryParam( Parameters.API_KEY ) String apiKey,
                                           @MatrixParam( "type" ) PhoneTypePayload type );

    /**
     * This POST method associates an email address with a POC. The POCHANDLE is the identifier of the POC record to
     * which the new email address will be added. The EMAIL to be added is provided in the URL. If successful, the
     * response contains a payload representing the POC, which will in turn contain an EMAILS collection that lists all
     * of the POC's email addresses, including the one added by submission of this method.<br/>
     * <br/>
     * This method is convenient when the user wants to add an email address to a POC but does not want to respecify the
     * entire POC record via a POC MODIFY (PUT) request. This method does not destroy existing EMAIL records, rather it
     * simply adds an additional EMAIL.<br/>
     * <br/>
     * If a validation error occurs, the EMAIL is not added and an HTTP 400 (bad request) response containing an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned along with an error code and message
     * explaining the error.<br/>
     * <br/>
     * The APIKEY used in the request must belong to an ARIN Online user that is linked to the POC. An attempt to modify
     * a POC (by adding an EMAIL) for which such a link does not exist will result in an HTTP 401 (unauthorized)
     * response containing an E_AUTHENTICATION {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. Invalid or
     * inactivated APIKEYs will result in an HTTP 400 (bad request) response.<br/>
     *
     * @param pocHandle The pocHandle from the RESTful path
     * @param apiKey    The apiKey that came in from the client
     * @param email     The email to add from the RESTful path
     * @return A POC payload showing the current state of the record
     */
    @POST
    @Path( "/{" + Parameters.POC_HANDLE + "}/email/{" + Parameters.EMAIL + "}" )
    PocPayload addEmail( @PathParam( Parameters.POC_HANDLE ) String pocHandle,
                         @PathParam( Parameters.EMAIL ) String email,
                         @QueryParam( Parameters.API_KEY ) String apiKey );

    /**
     * This DELETE method removes an email address from a POC. The POCHANDLE is the identifier of the POC record from
     * which the EMAIL will be removed. The EMAIL to be removed is provided in the URL. If successful, the response
     * contains a payload representing the POC, which will in turn contain an EMAILS collection that lists all of the
     * POC's remaining email addresses (no longer containing the one removed by the submission of this method).<br/>
     * <br/>
     * The resulting POC record must still pass validation before the changes are accepted and the service returns a
     * success (HTTP 200) response. For example, if the submission attempts to remove the only email address for a POC,
     * it fails with a validation error (because every POC must have at least one email address).
     * <br>
     * If a validation error occurs, the EMAIL is not removed and an HTTP 400 (bad request) response containing an
     * {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload} is returned along with an error code and message
     * explaining the error.<br/>
     * <br/>
     * The APIKEY used in the request must belong to an ARIN Online user that is linked to the POC. An attempt to modify
     * a POC (by removing an EMAIL) for which such a link does not exist will result in an HTTP 401 (unauthorized)
     * response containing an E_AUTHENTICATION {@link net.arin.tp.api.payload.ErrorPayload ErrorPayload}. Invalid or
     * inactivated APIKEYs will result in an HTTP 400 (bad request) response.<br/>
     *
     * @param pocHandle The pocHandle from the RESTful path
     * @param apiKey    The apiKey that came in from the client
     * @param email     The email to delete from the RESTful path
     * @return A POC payload showing the current state of the record
     */
    @DELETE
    @Path( "/{" + Parameters.POC_HANDLE + "}/email/{" + Parameters.EMAIL + "}" )
    PocPayload deleteEmail( @PathParam( Parameters.POC_HANDLE ) String pocHandle,
                            @PathParam( Parameters.EMAIL ) String email,
                            @QueryParam( Parameters.API_KEY ) String apiKey );
}
