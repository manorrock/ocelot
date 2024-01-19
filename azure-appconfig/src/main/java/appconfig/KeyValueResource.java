package appconfig;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import java.util.HashMap;

/**
 * REST API for Azure App Configuration.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("kv")
@Singleton
public class KeyValueResource {

    /**
     * Stores the key/value store.
     */
    private final HashMap<String, KeyValueBundle> kv = new HashMap<>();

    /**
     * Get the key value.
     *
     * <p>
     * For more information, see
     * https://learn.microsoft.com/en-us/azure/azure-app-configuration/rest-api-key-value#get-key-value
     * </p>
     *
     * @param key the key.
     * @param authorization the 'Authorization' header.
     * @return the key-value bundle.
     */
    @Path("{key}")
    @GET
    public Response getKeyValue(
            @PathParam("key") String key,
            @HeaderParam("Authorization") String authorization) {

        if (authorization == null) {
            return sendAuthenticateResponse();
        }

        return Response.ok(kv.get(key)).header("Connection", "close").build();
    }

    /**
     * Set the key.
     *
     * <p>
     * For more inforation, see
     * https://learn.microsoft.com/en-us/azure/azure-app-configuration/rest-api-key-value#set-key
     * </p>
     *
     * @param key the key.
     * @param authorization the 'Authorization' header.
     * @param body the JSON body.
     * @return the key-value bundle.
     */
    @Path("{key}")
    @PUT
    public Response setKeyValue(
            @PathParam("key") String key,
            @HeaderParam("Authorization") String authorization,
            KeyValueBundle body) {

        if (authorization == null) {
            return sendAuthenticateResponse();
        }

        kv.put(key, body);
        return Response.ok(body).header("Connection", "close").build();
    }

    /**
     * Send a response asking for authentication.
     * 
     * @return the authenticate response.
     */
    private Response sendAuthenticateResponse() {
        Error error = new Error();
        error.setCode("Unauthorized");
        error.setMessage("Request is missing a Bearer or PoP token.");
        return Response
                .status(UNAUTHORIZED)
                .entity(error)
                .header("WWW-Authenticate", "Bearer authorization=\"https://localhost:8200/appconfig\", resource=\"https://appconfig.azure.net\"")
                .header("Connection", "close")
                .build();
    }
}
