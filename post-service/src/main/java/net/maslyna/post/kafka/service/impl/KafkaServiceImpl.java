package net.maslyna.post.kafka.service.impl;

import lombok.RequiredArgsConstructor;
import net.maslyna.post.kafka.dto.CommentLikedResponse;
import net.maslyna.post.kafka.dto.PostLikedResponse;
import net.maslyna.post.kafka.service.KafkaService;
import net.maslyna.post.mapper.PostMapper;
import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.post.Post;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PostMapper postMapper;

    @Value("${spring.kafka.topics.notification.post}")
    private String postCreatedTopic;
    @Value("${spring.kafka.topics.notification.post-liked}")
    private String postLikedTopic;
    @Value("${spring.kafka.topics.notification.comment-liked}")
    private String commentLikedTopic;

    @Override
    public void sendPostCreated(Post post) {
        kafkaTemplate.send(postCreatedTopic, postMapper.postToPostCreatedResponse(post));
    }

    @Override
    public void sendPostLiked(Long authUser, Post post) {
        kafkaTemplate.send(postLikedTopic,
                PostLikedResponse.builder()
                        .postOwnerId(post.getUserId())
                        .userId(authUser)
                        .build()
        );
    }

    @Override
    public void sendCommentLiked(Long authenticatedUserId, Comment comment) {
        kafkaTemplate.send(commentLikedTopic,
                CommentLikedResponse.builder()
                        .userId(authenticatedUserId)
                        .commentId(comment.getId())
                        .postId(comment.getPost().getId())
                        .build()
        );
    }
}
