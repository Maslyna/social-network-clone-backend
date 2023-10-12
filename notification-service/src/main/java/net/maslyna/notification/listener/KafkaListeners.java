package net.maslyna.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListeners {

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification.notification-send.post-created}' }",
            groupId = "post",
            containerFactory = "kafkaListenerFactory"
    )
    public void handlerPostMessageSendingEvent(List<String> emails) {
//        PostCreatedResponse response = objectMapper.readValue(o, PostCreatedResponse.class);
        log.info("emails = {}", emails);
    }
}
