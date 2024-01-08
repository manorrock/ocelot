package appconfig;

import com.azure.core.credential.BasicAuthenticationCredential;
import com.azure.core.credential.TokenCredential;
import static com.azure.core.http.policy.HttpLogDetailLevel.BODY_AND_HEADERS;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.data.appconfiguration.ConfigurationClient;
import com.azure.data.appconfiguration.ConfigurationClientBuilder;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The integration tests to test our simulated Azure App Config.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class AppConfigIT {

    private TokenCredential credential;
    
    public AppConfigIT() {
    }

    @BeforeEach
    public void setUp() {
        credential = new BasicAuthenticationCredential("", "");
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetKeyValue() {
        String endpoint = "https://localhost:8201/api";

        ConfigurationClient configClient = new ConfigurationClientBuilder()
                .endpoint(endpoint)
                .credential(credential)
                .httpLogOptions(new HttpLogOptions().setLogLevel(BODY_AND_HEADERS))
                .buildClient();
        
        configClient.setConfigurationSetting("key", "label", "my_value");
        assertEquals("my_value", configClient.getConfigurationSetting("key", "label").getValue());
    }
}
