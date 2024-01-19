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
 * REST API for Azure KeyVault Secrets.
 *
 * <p>
 * Reworked as the Azure SDK for .NET does validation on the secret id and as a
 * consequence we cannot host multiple key vaults on the same base URL. See
 * https://github.com/Azure/azure-sdk-for-net/blob/4abfa9feb47a6422ba627ca6517ed3e4014c67f9/sdk/keyvault/Azure.Security.KeyVault.Shared/src/KeyVaultIdentifier.cs#L54
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("secrets")
@Singleton
public class SecretResource {

    /**
     * Stores the secrets.
     */
    private final Map<String, SecretBundle> secrets = new HashMap<>();

    /**
     * Get the secret.
     *
     * <p>
     * For more information, see
     * https://learn.microsoft.com/en-us/rest/api/keyvault/secrets/get-secret/get-secret?tabs=HTTP
     * </p>
     *
     * @param secretName the secret name.
     * @param authorization the 'Authorization' header.
     * @return the secret value.
     */
    @Path("{secretName}")
    @GET
    public Response getSecret(
            @PathParam("secretName") String secretName,
            @HeaderParam("Authorization") String authorization) {
        return getSecretWithVersion(secretName, null, authorization);
    }

    /**
     * Get the secret.
     *
     * <p>
     * For more information, see
     * https://learn.microsoft.com/en-us/rest/api/keyvault/secrets/get-secret/get-secret?tabs=HTTP
     * </p>
     *
     * @param secretName the secret name.
     * @param secretVersion the secret version.
     * @param authorization the 'Authorization' header.
     * @return the secret value.
     */
    @Path("{secretName}/{secretVersion}")
    @GET
    public Response getSecretWithVersion(
            @PathParam("secretName") String secretName,
            @PathParam("secretVersion") String secretVersion,
            @HeaderParam("Authorization") String authorization) {

        /*
         * See https://github.com/Azure/azure-sdk-for-java/blob/789fef47a9de372fda4fbbb4185b4e8d179017c0/sdk/keyvault/azure-security-keyvault-keys/src/main/java/com/azure/security/keyvault/keys/implementation/KeyVaultCredentialPolicy.java#L118
         */
        if (authorization == null) {
            return sendAuthenticateResponse();
        }
        
        SecretBundle secret = secrets.get(secretName);
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
     * @param secretName the secret name.
     * @param authorization the 'Authorization' header.
     * @param inputStream the input stream.
     * @return the response.
     */
    @Path("{secretName}")
    @PUT
    public Response setSecret(
            @PathParam("secretName") String secretName,
            @HeaderParam("Authorization") String authorization,
            InputStream inputStream) {

        /*
         * See https://github.com/Azure/azure-sdk-for-java/blob/789fef47a9de372fda4fbbb4185b4e8d179017c0/sdk/keyvault/azure-security-keyvault-keys/src/main/java/com/azure/security/keyvault/keys/implementation/KeyVaultCredentialPolicy.java#L118
         */
        if (authorization == null) {
            return sendAuthenticateResponse();
        }

        Jsonb jsonb = JsonbBuilder.create();
        SecretBundle secret;
        
        try { 
            secret = jsonb.fromJson(inputStream, SecretBundle.class);
        } catch(JsonbException je) {
            throw new WebApplicationException(je, 500);
        }
        
        if (secret.getId() == null) {
            secret.setId(getBaseUrl() + "/secrets/" + secretName + "/1");
        }

        secrets.put(secretName, secret);
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
                .header("WWW-Authenticate", "Bearer authorization=\"https://localhost:8200/keyvault\", resource=\"https://appconfig.azure.net\"")
                .header("Connection", "close")
                .build();
    }
}
