package keyvault;

import com.azure.core.credential.BasicAuthenticationCredential;
import com.azure.core.credential.TokenCredential;
import static com.azure.core.http.policy.HttpLogDetailLevel.BODY_AND_HEADERS;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The integration tests to test our simulated Azure Key Vault.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class KeyVaultIT {

    private TokenCredential credential;
    
    public KeyVaultIT() {
    }

    @BeforeEach
    public void setUp() {
        credential = new BasicAuthenticationCredential("", "");
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetSecret() {
        String keyVaultUri = "https://localhost:8200/api/keyvault/myKeyvault";

        SecretClient keyClient = new SecretClientBuilder()
                .vaultUrl(keyVaultUri)
                .credential(credential)
                .httpLogOptions(new HttpLogOptions().setLogLevel(BODY_AND_HEADERS))
                .buildClient();
        
        keyClient.setSecret("mySecret", "mySecretValue");
        assertEquals("mySecretValue", keyClient.getSecret("mySecret").getValue());
    }
}
