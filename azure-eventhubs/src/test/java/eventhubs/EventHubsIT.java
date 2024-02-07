package eventhubs;

import com.azure.core.credential.BasicAuthenticationCredential;
import com.azure.core.credential.TokenCredential;
import com.azure.core.util.IterableStream;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerClient;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.azure.messaging.eventhubs.models.EventPosition;
import com.azure.messaging.eventhubs.models.PartitionEvent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The integration tests to test our simulated Azure Event Hubs.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EventHubsIT {

    /**
     * Stores the credential.
     */
    private TokenCredential credential;

    /**
     * Setup the credential.
     */
    @BeforeEach
    public void setUp() {
        credential = new BasicAuthenticationCredential("username", "password");
    }

    /**
     * Test sending and receiving an event.
     */
    @Test
    public void testSendAndReceive() {
        try (EventHubProducerClient producer = new EventHubClientBuilder()
                .connectionString("Endpoint=sb://localhost/;SharedAccessKeyName=Name;SharedAccessKey=Value", "simulator")
                .credential(credential)
                .buildProducerClient()) {
            EventDataBatch batch = producer.createBatch();
            batch.tryAdd(new EventData("Send an event"));
            producer.send(batch);
        }

        EventHubConsumerClient consumer = new EventHubClientBuilder()
                .connectionString("Endpoint=sb://localhost/;SharedAccessKeyName=Name;SharedAccessKey=Value", "simulator")
                .credential(credential)
                .prefetchCount(1)
                .consumerGroup("$DEFAULT")
                .buildConsumerClient();

        String partitionId = consumer.getPartitionIds().iterator().next();

        IterableStream<PartitionEvent> events
                = consumer.receiveFromPartition(partitionId, 1, EventPosition.earliest());
        if (events.iterator().hasNext()) {
            EventData eventData = events.iterator().next().getData();
            assertEquals(eventData.getBodyAsString(), "Send an event");
        }
        fail("No event found");
    }
}
