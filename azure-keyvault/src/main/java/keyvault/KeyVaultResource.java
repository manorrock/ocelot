package keyvault;

import jakarta.inject.Singleton;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
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
     * @param contentLength the content length.
     * @param keyVault the key vault.
     * @param secretName the secret name.
     * @param inputStream the input stream.
     * @return the response.
     */
    @Path("{name}/secrets/{secretName}")
    @PUT
    public SecretBundle setSecret(
            @HeaderParam("Content-Length") Integer contentLength,
            @PathParam("name") String keyVault,
            @PathParam("secretName") String secretName, InputStream inputStream) {

        SecretBundle secret;

        if (contentLength != null && contentLength > 0) {
            Jsonb jsonb = JsonbBuilder.create();
            secret = jsonb.fromJson(inputStream, SecretBundle.class);
        } else {
            throw new WebApplicationException(500);
        }

        Map<String, SecretBundle> secretsMap = secrets.get(keyVault);
        if (secretsMap == null) {
            secretsMap = new HashMap<>();
            secrets.put(keyVault, secretsMap);
        }

        secretsMap.put(secretName, secret);
        return secret;
    }
}
