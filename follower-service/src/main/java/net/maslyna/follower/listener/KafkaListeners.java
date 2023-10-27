package net.maslyna.follower.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.follower.client.UserClient;
import net.maslyna.common.response.UserResponse;
import net.maslyna.follower.model.entity.User;
import net.maslyna.follower.producer.KafkaProducer;
import net.maslyna.follower.service.FollowerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListeners {
    private final FollowerService followerService;
    private final KafkaProducer producer;
    private final UserClient userClient;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification.post}' }",
            groupId = "post",
            containerFactory = "kafkaListenerFactory"
    )
    @Transactional(readOnly = true)
    public void listener(PostCreatedEvent event) {
        log.debug("post created event = {}", event);

        List<String> emails = getFollowersEmails(event.userId());

        if (!emails.isEmpty()) {
            producer.sendPostNotificationsEvent(event, emails);
            log.debug("notifications were sent for emails = {}", emails);
        }
    }

    private List<String> getFollowersEmails(Long userId) {
        return followerService.getFollowers(userId)
                .stream()
                .map(User::getId)
                .map(userClient::getUserById)
                .map(UserResponse::email)
                .filter(Objects::nonNull)
                .toList();
    }
}
