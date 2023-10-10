package net.maslyna.post.service;

import net.maslyna.post.model.entity.like.CommentLike;
import net.maslyna.post.model.entity.like.PostLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface LikeService {
    UUID likePost(Long authenticatedUserId, UUID postId);

    UUID likeComment(Long authenticatedUserId, UUID commentId);

    void deleteLikeFromPost(Long authenticatedUserId, UUID postId);

    void deleteLikeFromComment(Long authenticatedUserId, UUID commentId);

    @Transactional(readOnly = true)
    Page<PostLike> getLikesOnPost(
            UUID postId,
            Long authenticatedUserId,
            PageRequest pageRequest);

    @Transactional(readOnly = true)
    Page<CommentLike> getLikesOnComment(
            UUID commentId,
            PageRequest pageRequest);
}
