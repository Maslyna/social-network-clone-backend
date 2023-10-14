package net.maslyna.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.kafka.dto.CommentLikedEvent;
import net.maslyna.common.kafka.dto.PostLikedEvent;
import net.maslyna.common.kafka.dto.PostNotificationEvent;
import net.maslyna.notification.client.UserClient;
import net.maslyna.notification.service.EmailService;
import net.maslyna.notification.service.HtmlTemplateService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

import static net.maslyna.notification.model.HtmlTemplate.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaListeners {
    private final EmailService emailService;
    private final HtmlTemplateService templateService;
    private final UserClient userClient;

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification.notification-send.post-created}' }",
            groupId = "post",
            containerFactory = "kafkaListenerFactory"
    )
    public void handlerPostCreatedEvent(PostNotificationEvent event) {
        log.info("postCreatedEvent = {}", event);

        try {
            emailService.sendEmail(
                    POST_CREATED.getSubject(),
                    templateService.getHtmlTemplate(event.postInfo()),
                    event.emails()
            );
        } catch (MailSendException e) {
            log.error("Mail send exception {}", Arrays.toString(e.getStackTrace()));
        } catch (IOException exception) {
            log.error("IO exception template {}, error {}", POST_CREATED, Arrays.toString(exception.getStackTrace()));
        }
    }

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification.notification-send.post-liked}' }",
            groupId = "post",
            containerFactory = "kafkaListenerFactory"
    )
    public void handlePostLikedEvent(PostLikedEvent event) {
        log.info("postLikedEvent = {}", event);

        try {
            emailService.sendEmail(
                    POST_LIKED.getSubject(),
                    templateService.getHtmlTemplate(event),
                    userClient.getUserById(event.postOwnerId()).email()
            );
        } catch (MailSendException e) {
            log.error("Mail send exception {}", Arrays.toString(e.getStackTrace()));
        } catch (IOException exception) {
            log.error("IO exception template {}, error {}", POST_LIKED, Arrays.toString(exception.getStackTrace()));
        }
    }

    @KafkaListener(
            topics = "#{ '${spring.kafka.topics.notification.notification-send.comment-liked}' }",
            groupId = "post",
            containerFactory = "kafkaListenerFactory"
    )
    public void handleCommentLikedEvent(CommentLikedEvent event) {
        log.info("commentLikedEvent = {}", event);

        try {
            emailService.sendEmail(
                    POST_LIKED.getSubject(),
                    templateService.getHtmlTemplate(event),
                    userClient.getUserById(event.commentOwnerId()).email()
            );
        } catch (MailSendException e) {
            log.error("Mail send exception {}", Arrays.toString(e.getStackTrace()));
        } catch (IOException exception) {
            log.error("IO exception template {}, error {}", COMMENT_LIKED, Arrays.toString(exception.getStackTrace()));
        }
    }
}
