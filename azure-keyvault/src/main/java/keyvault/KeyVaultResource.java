package keyvault;

import jakarta.inject.Singleton;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
<<<<<<< Updated upstream
import jakarta.servlet.http.HttpServletResponse;
=======
import jakarta.json.bind.JsonbException;
>>>>>>> Stashed changes
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * REST API for Azure KeyVault.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("keyvault")
@Singleton
public class KeyVaultResource {

    /**
     * Stores the secrets.
     */
    private Map<String, Map<String, SecretBundle>> secrets = new HashMap<>();

    /**
     * Get the secret.
     *
     * <p>
     * For more information, see
     * https://learn.microsoft.com/en-us/rest/api/keyvault/secrets/get-secret/get-secret?tabs=HTTP
     * </p>
     *
     * @param keyVault the key vault.
     * @param secretName the secret name.
     * @return the secret value.
     */
    @Path("{name}/secrets/{secretName}")
    @GET
    public SecretBundle getSecret(
            @PathParam("name") String keyVault,
            @PathParam("secretName") String secretName) {
        return getSecretWithVersion(keyVault, secretName, null);
    }

    /**
     * Get the secret.
     *
     * <p>
     * For more information, see
     * https://learn.microsoft.com/en-us/rest/api/keyvault/secrets/get-secret/get-secret?tabs=HTTP
     * </p>
     *
     * @param keyVault the key vault.
     * @param secretName the secret name.
     * @param secretVersion the secret version.
     * @return the secret value.
     */
    @Path("{name}/secrets/{secretName}/{secretVersion}")
    @GET
    public SecretBundle getSecretWithVersion(
            @PathParam("name") String keyVault,
            @PathParam("secretName") String secretName,
            @PathParam("secretVersion") String secretVersion) {
        SecretBundle secret = null;
        Map<String, SecretBundle> secretsMap = secrets.get(keyVault);
        if (secretsMap != null) {
            secret = secretsMap.get(secretName);
        }
        return secret;
    }

    /**
     * Set the secret.
     *
     * <p>
     * For more information, see
     * https://learn.microsoft.com/en-us/rest/api/keyvault/secrets/set-secret/set-secret?tabs=HTTP
     * </p>
     *
     * @param keyVault the key vault.
     * @param secretName the secret name.
     * @param inputStream the input stream.
     * @return the response.
     */
    @Path("{name}/secrets/{secretName}")
    @PUT
    public SecretBundle setSecret(
            @PathParam("name") String keyVault,
            @PathParam("secretName") String secretName, 
            InputStream inputStream) {

        Jsonb jsonb = JsonbBuilder.create();
        SecretBundle secret;
        
        try { 
            secret = jsonb.fromJson(inputStream, SecretBundle.class);
        } catch(JsonbException je) {
            throw new WebApplicationException(je, 500);
        }
        
        if (secret.getId() == null) {
            secret.setId(getBaseUrl() + "/keyvault/" + keyVault + "/secrets/" + secretName + "/1");
        }

        Map<String, SecretBundle> secretsMap = secrets.get(keyVault);
        if (secretsMap == null) {
            secretsMap = new HashMap<>();
            secrets.put(keyVault, secretsMap);
        }

        secretsMap.put(secretName, secret);
        return secret;
    }

    /**
     * Get the base URL.
     * 
     * @return the base URL.
     */
    private String getBaseUrl() {
        String baseUrl = System.getenv("BASE_URL");
        if (baseUrl == null) {
            baseUrl = "https://localhost:8200";
        }
        return baseUrl;
    }
}
