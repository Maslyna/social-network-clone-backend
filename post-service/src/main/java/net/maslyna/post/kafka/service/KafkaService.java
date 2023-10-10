package net.maslyna.post.kafka.service;

import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.post.Post;

public interface KafkaService {
    void sendPostCreated(Post post);

    void sendPostLiked(Long authUser, Post post);

    void sendCommentLiked(Long authenticatedUserId, Comment comment);
}
