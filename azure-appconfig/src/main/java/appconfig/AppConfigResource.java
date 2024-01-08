package appconfig;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * REST API for Azure App Configuration.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("kv")
@Singleton
public class AppConfigResource {

    /**
     * Get the key value.
     *
     * <p>
     * For more information, see
     * https://learn.microsoft.com/en-us/azure/azure-app-configuration/rest-api-key-value#get-key-value
     * </p>
     *
     * @param key the key.
     * @return the value.
     */
    @Path("{key}")
    @GET
    public KeyValueBundle getKeyValue(
            @PathParam("key") String key) {
        KeyValueBundle bundle = new KeyValueBundle();
        bundle.setKey(key);
        bundle.setValue("my_value");
        return bundle;
    }
}
