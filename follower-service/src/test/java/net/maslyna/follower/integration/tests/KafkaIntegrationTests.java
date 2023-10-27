package net.maslyna.follower.integration.tests;

import org.junit.jupiter.api.Test;

public class KafkaIntegrationTests extends BasicIntegrationTest {

    @Test
    public void postCreatedEventTests() throws Exception {
        testKafkaProducer.sentTestPostNotificationEvent();
    }
}
