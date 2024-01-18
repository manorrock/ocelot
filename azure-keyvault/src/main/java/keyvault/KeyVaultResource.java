package keyvault;

import jakarta.inject.Singleton;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
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
    public Response getSecret(
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
    public Response getSecretWithVersion(
            @PathParam("name") String keyVault,
            @PathParam("secretName") String secretName,
            @PathParam("secretVersion") String secretVersion) {
        
        SecretBundle secret = null;
        Map<String, SecretBundle> secretsMap = secrets.get(keyVault);
        if (secretsMap != null) {
            secret = secretsMap.get(secretName);
        }
        return Response.ok(secret).header("Connection", "close").build();
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
     * @param contentLength the content length.
     * @return the response.
     */
    @Path("{name}/secrets/{secretName}")
    @PUT
    public Response setSecret(
            @PathParam("name") String keyVault,
            @PathParam("secretName") String secretName, 
            @HeaderParam("Content-Length") Integer contentLength,
            InputStream inputStream) {

        /*
         * See https://github.com/Azure/azure-sdk-for-java/blob/789fef47a9de372fda4fbbb4185b4e8d179017c0/sdk/keyvault/azure-security-keyvault-keys/src/main/java/com/azure/security/keyvault/keys/implementation/KeyVaultCredentialPolicy.java#L118
         */
        if (contentLength == null || contentLength == 0) {
            Error error = new Error();
            error.setCode("Unauthorized");
            error.setMessage("AKV10000: Request is missing a Bearer or PoP token.");
            return Response
                    .status(UNAUTHORIZED)
                    .entity(error)
                    .header("WWW-Authenticate", "Bearer authorization=\"https://localhost:8200/keyvault\", resource=\"https://vault.azure.net\"")
                    .header("Connection", "close")
                    .build();
        }
        
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
        return Response.ok(secret).header("Connection", "close").build();
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
