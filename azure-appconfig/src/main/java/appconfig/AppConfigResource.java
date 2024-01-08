package appconfig;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.HashMap;

/**
 * REST API for Azure App Configuration.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("kv")
@Singleton
public class AppConfigResource {
    
    /**
     * Stores the key/value store.
     */
    private HashMap<String, KeyValueBundle> kv = new HashMap<>();

    /**
     * Get the key value.
     *
     * <p>
     * For more information, see
     * https://learn.microsoft.com/en-us/azure/azure-app-configuration/rest-api-key-value#get-key-value
     * </p>
     *
     * @param key the key.
     * @return the key-value bundle.
     */
    @Path("{key}")
    @GET
    public KeyValueBundle getKeyValue(
            @PathParam("key") String key) {
        return kv.get(key);
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
     * @param body the JSON body.
     * @return the key-value bundle.
     */
    @Path("{key}")
    @PUT
    public KeyValueBundle setKeyValue(
        @PathParam("key") String key, KeyValueBundle body) {
        kv.put(key, body);
        return body;
    }
}
