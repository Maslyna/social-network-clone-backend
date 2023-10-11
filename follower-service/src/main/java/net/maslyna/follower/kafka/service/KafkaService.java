package net.maslyna.follower.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.notification.notification-send.post-created}")
    private String postCreatedTopic;

    public void sendPostCreatedNotificationsSendingEvent(List<String> emails) {
        kafkaTemplate.send(postCreatedTopic, emails);
    }
}
