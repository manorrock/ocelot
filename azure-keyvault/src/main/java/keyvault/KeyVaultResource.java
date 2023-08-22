package keyvault;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * REST API for Azure KeyVault.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("keyvault")
public class KeyVaultResource {

    /**
     * Get the secret.
     * 
     * <p>
     *  For more information, see https://learn.microsoft.com/en-us/rest/api/keyvault/secrets/get-secret/get-secret?tabs=HTTP
     * </p>
     * 
     * @param keyVault the key vault.
     * @param secretName the secret name.
     * @return the secret value.
     */
    @Path("{name}/secrets/{secretName}")
    @GET
    public KeyVaultSecret get(
            @PathParam("name") String keyVault, 
            @PathParam("secretName") String secretName) {
        return new KeyVaultSecret("secretValue");
    }
}
