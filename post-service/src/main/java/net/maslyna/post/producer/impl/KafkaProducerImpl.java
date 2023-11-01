package net.maslyna.post.producer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.kafka.dto.CommentLikedEvent;
import net.maslyna.common.kafka.dto.PostLikedEvent;
import net.maslyna.post.mapper.PostMapper;
import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.post.Post;
import net.maslyna.post.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PostMapper postMapper;

    @Value("${spring.kafka.topics.notification.post}")
    private String postCreatedTopic;
    @Value("${spring.kafka.topics.notification.post-liked}")
    private String postLikedTopic;
    @Value("${spring.kafka.topics.notification.comment-liked}")
    private String commentLikedTopic;

    @Override
    public void sendPostCreatedEvent(Post post) {
        if (post == null) {
            log.warn("given post must not be null");
            return;
        }
        send(postCreatedTopic, postMapper.postToPostCreatedResponse(post));
    }

    @Override
    public void sendPostLikedEvent(Long authenticatedUserId, Post post) {
        if (authenticatedUserId == null || post == null) {
            log.warn("authenticated user id or post equals null");
            return;
        }
        send(postLikedTopic,
                PostLikedEvent.builder() //TODO: mapper
                        .postOwnerId(post.getUserId())
                        .postId(post.getId())
                        .userId(authenticatedUserId)
                        .build()
        );
    }


    @Override
    public void sendCommentLikedEvent(Long authenticatedUserId, Comment comment) {
        if (authenticatedUserId == null || comment == null) {
            log.warn("authenticated user id or comment equals null");
            return;
        }
        send(commentLikedTopic,
                CommentLikedEvent.builder() //TODO: mapper
                        .userId(authenticatedUserId)
                        .commentId(comment.getId())
                        .postId(comment.getPost().getId())
                        .commentOwnerId(comment.getUserId())
                        .build()
        );
    }

    private void send(String topic, Object object) {
        try {
            log.info("send event with topic: {}, value: {}", topic, object);
            kafkaTemplate.send(topic, object);
        } catch (Exception e) {
            log.error("error sending kafka message {}", e.getMessage());
            log.error("stack trace: ", e);
        }
    }
}
