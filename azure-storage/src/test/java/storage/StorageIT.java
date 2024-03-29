package storage;

import com.azure.core.credential.BasicAuthenticationCredential;
import com.azure.core.credential.TokenCredential;
import static com.azure.core.http.policy.HttpLogDetailLevel.BODY_AND_HEADERS;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * The integration tests to test our simulated Azure Storage.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class StorageIT {

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
     * Test creating blob container.
     */
    @Test
    @Disabled
    public void testCreateBlobContainer() {
        
        BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
                .endpoint("https://localhost:8203")
                .credential(credential)
                .containerName("myBlobContainer")
                .httpLogOptions(new HttpLogOptions().setLogLevel(BODY_AND_HEADERS))
                .buildClient();
        
        blobContainerClient.create();
        assertEquals("myBlobContainer", blobContainerClient.getBlobContainerName());
    }
}
