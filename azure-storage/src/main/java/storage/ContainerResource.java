package storage;

import jakarta.inject.Singleton;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import static jakarta.ws.rs.core.Response.Status.CREATED;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("")
@Singleton
public class ContainerResource {

    /**
     * Stores the secrets.
     */
    private final List<String> containerNames = new ArrayList<>();

    /**
     * Create the container.
     *
     * @param containerName the container name.
     * @return the response.
     */
    @Path("{containerName}?restype=container")
    @PUT
    public Response createContainer(
            @PathParam("containerName") String containerName) {

        if (!containerNames.contains(containerName)) {
            containerNames.add(containerName);
            return Response.status(CREATED).header("Connection", "close").build();
        }
        return Response.serverError().build();
    }
}
