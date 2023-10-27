package net.maslyna.follower.integration.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.kafka.dto.PostCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@TestComponent
@Slf4j
public class TestKafkaProducer {
    @Value("${spring.kafka.topics.notification.post}")
    private String postNotificationTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final static PostCreatedEvent testPostCreatedEvent = PostCreatedEvent.builder()
            .post(UUID.randomUUID())
            .rePost(UUID.randomUUID())
            .createdAt(Instant.now())
            .userId(1L)
            .build();

    public void sentTestPostNotificationEvent() {
        log.info("send test post notification event");
        kafkaTemplate.send(
                postNotificationTopic,
                testPostCreatedEvent
        );
    }
}
