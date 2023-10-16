package net.maslyna.post.integration;

import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.post.integration.consumer.KafkaTestConsumer;
import net.maslyna.post.model.PostStatus;
import net.maslyna.post.model.entity.post.Post;
import net.maslyna.post.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class KafkaTestContainersLiveTest extends BasicIntegrationTest {

    @Autowired
    private KafkaTestConsumer consumer;

    @Autowired
    private KafkaProducer producer;

    private static final Post testPost = Post.builder()
            .id(UUID.randomUUID())
            .userId(1L)
            .status(PostStatus.PUBLISHED)
            .title("test title")
            .text("test text")
            .createdAt(Instant.now())
            .comments(Set.of())
            .likes(Set.of())
            .hashtags(Set.of())
            .build();


    @Test
    public void sendPostCreatedEvent_thenMessageReceived() throws Exception {

        producer.sendPostCreatedEvent(testPost);

        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);

        assertTrue(messageConsumed);
        assertEquals(consumer.getPayload().getClass(), PostCreatedEvent.class);

        PostCreatedEvent event = (PostCreatedEvent) consumer.getPayload();

        assertEquals(testPost.getId(), event.post());
        assertEquals(testPost.getUserId(), event.userId());
        assertEquals(testPost.getTitle(), event.title());
    }
}