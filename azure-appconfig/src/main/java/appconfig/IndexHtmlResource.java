package appconfig;

import jakarta.inject.Singleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Expose the /index.html resource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Path("")
@Singleton
public class IndexHtmlResource {

    /**
     * Constructor.
     */
    public IndexHtmlResource() {
    }

    /**
     * /index.html.
     * 
     * @param request the HTTP servlet request.
     * @return ""
     */
    @Path(value = "")
    @GET
    @Produces(value = "text/html")
    public StreamingOutput indexHtml(@Context HttpServletRequest request) {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                InputStream inputStream = request.getServletContext().getResourceAsStream("/index.html");
                inputStream.transferTo(output);
            }
        };
    }
}
