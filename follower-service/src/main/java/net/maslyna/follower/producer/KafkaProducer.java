package net.maslyna.follower.producer;

import net.maslyna.common.kafka.dto.PostCreatedEvent;

import java.util.List;

public interface KafkaProducer {
    void sendPostNotificationsEvent(PostCreatedEvent event, List<String> emails);
}
