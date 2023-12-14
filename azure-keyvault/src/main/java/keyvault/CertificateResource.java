package keyvault;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Certificate download endpoint.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("certificate")
public class CertificateResource {

    /**
     * Download the certificate
     * 
     * @return the certificate.
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
