package keyvault;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

/**
 * Ping resource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("ping")
@Singleton
public class PingResource {

    /**
     * Constructor.
     */
    public PingResource() {
    }

    /**
     * Ping.
     * 
     * @return "OK"
     */
    @Path(value = "")
    @GET
    @Produces(value = "text/plain")
    public String ping() {
        return "OK";
    }
}
