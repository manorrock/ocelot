package appconfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * The JUnit test for the PingResource class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class PingResourceIT {

    /**
     * Ping the resource.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testPing() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:8101/ping")).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertEquals("OK", response.body());
    }
}
