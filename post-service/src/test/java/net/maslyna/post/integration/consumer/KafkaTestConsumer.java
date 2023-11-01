package net.maslyna.post.integration.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.kafka.dto.CommentLikedEvent;
import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.common.kafka.dto.PostLikedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;

@Getter
@TestComponent
@Slf4j
@ActiveProfiles("test")
public class KafkaTestConsumer {
    private CountDownLatch latch = new CountDownLatch(1);
    private Object payload;
    @Autowired
    private ObjectMapper mapper;


    @KafkaListener(
            topics = "${spring.kafka.topics.notification.post}"
    )
    public void postCreatedEventHandler(String event) throws JsonProcessingException {
        log.info("handled event = {}", event);

        payload = mapper.readValue(event, PostCreatedEvent.class);
        latch.countDown();
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.notification.post-liked}"
    )
    public void postLikedEventHandle(String event) throws JsonProcessingException {
        log.info("handled event = {}", event);

        payload = mapper.readValue(event, PostLikedEvent.class);
        latch.countDown();
    }

    @KafkaListener(
            topics = "${spring.kafka.topics.notification.comment-liked}"
    )
    public void commentLikedEventHandler(String event) throws JsonProcessingException {
        log.info("handled event = {}", event);
        payload = mapper.readValue(event, CommentLikedEvent.class);
        latch.countDown();
    }

    public void reset() {
        latch = new CountDownLatch(1);
        payload = null;
    }

}
