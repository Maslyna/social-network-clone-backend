package net.maslyna.post.service;

import net.maslyna.post.model.dto.request.CommentRequest;
import net.maslyna.post.model.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface CommentService {
    @Transactional(readOnly = true)
    Page<Comment> getComments(
            Long authenticatedUserId,
            UUID postId,
            PageRequest pageRequest
    );

    UUID postComment(
            Long authenticatedUserId,
            UUID postId,
            CommentRequest commentRequest
    );

    UUID postComment(
            Long authenticatedUserId,
            UUID postId,
            UUID commentId,
            CommentRequest commentRequest
    );

    UUID editComment(
            Long authenticatedUserId,
            UUID postId,
            UUID commentId,
            CommentRequest commentRequest
    );

    void deleteComment(
            Long authenticatedUserId,
            UUID commentId,
            UUID postId
    );

    Comment getComment(UUID commentId);

    void addPhoto(
            Long authenticatedUserId,
            UUID postId,
            UUID commentId,
            MultipartFile file
    );

    void removePhoto(
            Long authenticatedUserId,
            UUID postId,
            UUID commentId,
            UUID photoId
    );
}
