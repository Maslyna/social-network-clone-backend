//package net.maslyna.follower.integration.tests;
//
//import net.maslyna.common.kafka.dto.PostNotificationEvent;
//import org.junit.jupiter.api.Test;
//
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class KafkaIntegrationTests extends BasicIntegrationTest {
//
//    @Test
//    public void postCreatedEventTests() throws Exception {
//        testKafkaProducer.sentTestPostNotificationEvent();
//
//        boolean messageConsumed = testKafkaConsumer.getLatch().await(10, TimeUnit.SECONDS);
//        assertTrue(messageConsumed);
//
//        assertEquals(testKafkaConsumer.getPayload().getClass(), PostNotificationEvent.class);
//        PostNotificationEvent payload = (PostNotificationEvent) testKafkaConsumer.getPayload();
//
//        assertNotNull(payload.postInfo());
//        assertNotNull(payload.emails());
//
//        testKafkaConsumer.reset();
//    }
//}
// FIXME: 27.10.2023 