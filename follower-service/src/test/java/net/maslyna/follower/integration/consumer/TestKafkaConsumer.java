package net.maslyna.follower.integration.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.kafka.dto.PostNotificationEvent;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Getter
@RequiredArgsConstructor
@TestComponent
public class TestKafkaConsumer {
    private CountDownLatch latch = new CountDownLatch(1);
    private Object payload;
    private final ObjectMapper mapper;

    @KafkaListener(
            topics = "${spring.kafka.topics.notification.notification-send.post-created}"
    )
    public void postCreatedEventHandler(PostNotificationEvent event) {
        log.info("TEST handled event = {}", event);
        payload = event;
        latch.countDown();
    }

    public void reset() {
        latch = new CountDownLatch(1);
        payload = null;
    }

}
