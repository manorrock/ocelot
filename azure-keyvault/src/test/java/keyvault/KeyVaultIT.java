package keyvault;

import com.azure.core.credential.BasicAuthenticationCredential;
import com.azure.core.credential.TokenCredential;
import static com.azure.core.http.policy.HttpLogDetailLevel.BODY_AND_HEADERS;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The integration tests to test our simulated Azure Key Vault.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class KeyVaultIT {

    /**
     * Stores the credential.
     */
    private TokenCredential credential;

    /**
     * Setup the credential.
     */
    @BeforeEach
    public void setUp() {
        credential = new BasicAuthenticationCredential("username", "password");
    }

    /**
     * Test setting and then getting a secret.
     */
    @Test
    public void testGetSecret() {
        String keyVaultUri = "https://localhost:8200";

        SecretClient keyClient = new SecretClientBuilder()
                .vaultUrl(keyVaultUri)
                .credential(credential)
                .httpLogOptions(new HttpLogOptions().setLogLevel(BODY_AND_HEADERS))
                .disableChallengeResourceVerification()
                .buildClient();

        keyClient.setSecret("mySecret", "mySecretValue");
        assertEquals("mySecretValue", keyClient.getSecret("mySecret").getValue());
    }
}
