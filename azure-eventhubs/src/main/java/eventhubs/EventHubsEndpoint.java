package eventhubs;

/**
 * The EventHubs endpoint.
 * 
 * <p>
 *  For a reference example for Apache QPID Proton-J, see 
 *  https://github.com/apache/qpid-proton-j/tree/main/examples/engine
 * </p>
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EventHubsEndpoint implements Runnable {

    /**
     * Run the endpoint.
     */
    @Override
    public void run() {
    }
    
    /**
     * Main method (for testing).
     * 
     * @param arguments the arguments.
     */
    public static void main(String[] arguments) {
        EventHubsEndpoint endpoint = new EventHubsEndpoint();
        endpoint.run();
    }
}
