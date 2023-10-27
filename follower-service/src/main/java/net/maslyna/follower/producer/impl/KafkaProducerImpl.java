package net.maslyna.follower.producer.impl;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.common.kafka.dto.PostNotificationEvent;
import net.maslyna.follower.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.notification.notification-send.post-created}")
    private String postCreatedTopic;

    @Override
    public void sendPostNotificationsEvent(PostCreatedEvent event, List<String> emails) {
        kafkaTemplate.send(
                postCreatedTopic,
                PostNotificationEvent.builder()
                        .postInfo(event)
                        .emails(emails)
                        .build()
        );
    }
}
