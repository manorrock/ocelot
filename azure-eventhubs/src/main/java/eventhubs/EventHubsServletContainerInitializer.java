package eventhubs;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * The ServletContainerInitializer that initializes the AMQP endpoint.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EventHubsServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        System.err.println("Initializing EventHubs endpoint");
        EventHubsEndpoint endpoint = new EventHubsEndpoint();
        Executors.newSingleThreadExecutor().execute(endpoint);
    }
}
