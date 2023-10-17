package net.maslyna.post.producer;

import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.post.Post;

public interface KafkaProducer {
    void sendPostCreatedEvent(Post post);

    void sendPostLikedEvent(Long authenticatedUserId, Post post);

    void sendCommentLikedEvent(Long authenticatedUserId, Comment comment);
}
