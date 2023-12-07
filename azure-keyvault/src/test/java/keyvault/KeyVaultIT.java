package keyvault;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        credential = new DefaultAzureCredentialBuilder().build();
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    @Disabled
    public void testGetSecret() {
        String keyVaultUri = "https://localhost:8443/api";

        SecretClient keyClient = new SecretClientBuilder()
                .disableChallengeResourceVerification()
                .vaultUrl(keyVaultUri)
                .credential(credential)
                .buildClient();
        
        assertEquals("secretValue", keyClient.getSecret("secretKey"));
    }
}
