package net.maslyna.follower.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.follower.client.UserClient;
import net.maslyna.follower.client.UserResponse;
import net.maslyna.follower.exception.UserNotFoundException;
import net.maslyna.follower.kafka.service.KafkaService;
import net.maslyna.follower.model.entity.User;
import net.maslyna.follower.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListeners {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PropertiesMessageService messageService;
    private final KafkaService kafkaService;
    private final UserRepository repository;
    private final UserClient userClient;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification.post}' }",
            groupId = "post",
            containerFactory = "kafkaListenerFactory"
    )
    @Transactional
    public void listener(PostCreatedEvent event) {
        log.info("kafka listener (post-notification) post info = {}", event);
        User user = repository.findById(event.userId())
                .orElseThrow(() -> new UserNotFoundException(
                        HttpStatus.NOT_FOUND,
                        messageService.getProperty("error.user.not-found", event.userId())
                ));
        List<String> emails = user.getFollowers().stream()
                .map(User::getId)
                .map(userClient::getUserById) //TODO: create getAllUsersByIds in user service
                .map(UserResponse::email).toList();

        kafkaService.sendPostCreatedNotificationsSendingEvent(emails);
        log.info("notifications was sent for users with emails = {}", emails);
    }
}
