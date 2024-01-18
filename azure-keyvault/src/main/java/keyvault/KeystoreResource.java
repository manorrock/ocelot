package keyvault;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Keystore download endpoint.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("keystore")
@Singleton
public class KeystoreResource {

    /**
     * Constructor.
     */
    public KeystoreResource() {
    }
    
    /**
     * Download the keystore
     * 
     * @return the keystore.
     */
    @Path("")
    @GET
    public byte[] download() {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        try {
            java.nio.file.Path certificatePath = new File("/home/piranha/certs/keystore").toPath();
            Files.copy(certificatePath, byteOutput);
        } catch(IOException ioe) {
        }
        return byteOutput.toByteArray();
    }
}
