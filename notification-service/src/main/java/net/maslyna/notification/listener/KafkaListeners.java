package net.maslyna.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.kafka.dto.CommentLikedEvent;
import net.maslyna.common.kafka.dto.PostLikedEvent;
import net.maslyna.common.kafka.dto.PostNotificationEvent;
import net.maslyna.notification.model.HtmlTemplate;
import net.maslyna.notification.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListeners {
    private final EmailService emailService;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification.notification-send.post-created}' }",
            groupId = "post",
            containerFactory = "kafkaListenerFactory"
    )
    public void handlerPostCreatedEvent(PostNotificationEvent event) {
        //TODO: implement notification logic
        log.info("postCreatedEvent = {}", event);
        HtmlTemplate template = HtmlTemplate.POST_CREATED;

    }

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification.notification-send.post-liked}' }",
            groupId = "post",
            containerFactory = "kafkaListenerFactory"
    )
    public void handlePostLikedEvent(PostLikedEvent event) {
        log.info("postLikedEvent = {}", event);
    }

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification.notification-send.comment-liked}' }",
            groupId = "post",
            containerFactory = "kafkaListenerFactory"
    )
    public void handleCommentLikedEvent(CommentLikedEvent event) {
        log.info("commentLikedEvent = {}", event);
    }
}
