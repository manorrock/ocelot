package keyvault;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

/**
 * Ping resource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("ping")
public class PingResource {

    /**
     * Ping.
     * 
     * @return "OK"
     */
    @Path("")
    @GET
    @Produces("text/plain")
    public String ping() {
        return "OK";
    }
}
