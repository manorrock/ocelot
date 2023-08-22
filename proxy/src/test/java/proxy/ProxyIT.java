package proxy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ProxyIT {
    
    @Test
    void testHttpWwwMicrosoftDotCom() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://www.microsoft.com/")).build();
        BodyHandler bodyHandler = BodyHandlers.ofString();
        HttpResponse response = client.send(request, bodyHandler);
        assertEquals(200, response.statusCode());
    }
    
    @Disabled
    @Test
    void testHttpsWwwMicrosoftDotCom() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("https://www.microsoft.com/")).build();
        BodyHandler bodyHandler = BodyHandlers.ofString();
        HttpResponse response = client.send(request, bodyHandler);
        assertEquals(200, response.statusCode());
    }

    @Test
    void testPing() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:8080/")).build();
        BodyHandler bodyHandler = BodyHandlers.ofString();
        HttpResponse response = client.send(request, bodyHandler);
        assertEquals(200, response.statusCode());
    }
}
