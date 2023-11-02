package net.maslyna.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.model.FileType;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.post.exception.AccessDeniedException;
import net.maslyna.post.exception.CommentNotFoundException;
import net.maslyna.post.model.CommentStatus;
import net.maslyna.post.model.dto.request.CommentRequest;
import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.Photo;
import net.maslyna.post.model.entity.post.Post;
import net.maslyna.post.repository.CommentRepository;
import net.maslyna.post.service.CommentService;
import net.maslyna.post.service.PhotoService;
import net.maslyna.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final PropertiesMessageService messageService;
    private final PhotoService photoService;

    @Override
    @Transactional(readOnly = true)
    public Page<Comment> getComments(
            Long authenticatedUserId,
            UUID postId,
            PageRequest pageRequest
    ) {
        Post post = postService.getPost(authenticatedUserId, postId);
        List<Comment> comments = new ArrayList<>(post.getComments());
        return new PageImpl<>(comments, pageRequest, comments.size());
    }

    @Override
    public UUID postComment(
            Long authenticatedUserId,
            UUID postId,
            CommentRequest commentRequest
    ) {
        Post post = postService.getPost(authenticatedUserId, postId);
        Comment comment = createComment(authenticatedUserId, commentRequest.text());
        post.addComment(comment);
        comment.setPost(post);
        return comment.getId();
    }

    @Override
    public UUID postComment(
            Long authenticatedUserId,
            UUID postId,
            UUID commentId,
            CommentRequest commentRequest
    ) {
        Post post = postService.getPost(authenticatedUserId, postId);
        Comment comment = getComment(commentId);
        Comment newComment = createComment(authenticatedUserId, comment, commentRequest.text());
        newComment.setPost(post);
        comment.addComment(newComment);
        return newComment.getId();
    }

    @Override
    public UUID editComment(
            Long authenticatedUserId,
            UUID postId,
            UUID commentId,
            CommentRequest commentRequest
    ) {
        postService.checkIsPostExists(postId);
        Comment comment = getComment(commentId);
        if (!comment.getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException(
                    FORBIDDEN,
                    messageService.getProperty("error.access.denied")
            );
        }
        if (commentRequest.text() != null) {
            comment.setText(commentRequest.text());
            comment.setStatus(CommentStatus.EDITED);
        }
        return comment.getId();
    }

    @Override
    public void deleteComment(
            Long authenticatedUserId,
            UUID commentId,
            UUID postId
    ) {
        postService.checkIsPostExists(postId);
        Comment comment = getComment(commentId);
        if (!comment.getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException(
                    FORBIDDEN,
                    messageService.getProperty("error.access.denied")
            );
        }
        commentRepository.delete(comment);
    }

    @Override
    public Comment getComment(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(NOT_FOUND,
                        messageService.getProperty("error.comment.not-found", commentId)
                ));
    }

    @Override
    public void addPhoto(
            Long authenticatedUserId,
            UUID postId,
            UUID commentId,
            MultipartFile file
    ) {
        postService.checkIsPostExists(postId);
        Comment comment = getComment(commentId);
        if (!comment.getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException(
                    FORBIDDEN,
                    messageService.getProperty("error.access.denied")
            );
        }
        Photo photo = photoService.save(authenticatedUserId, FileType.POST_CONTENT, file);

        comment.setPhoto(photo);
    }

    @Override
    public void removePhoto(
            Long authenticatedUserId,
            UUID postId,
            UUID commentId,
            UUID photoId
    ) {
        postService.checkIsPostExists(postId);
        Comment comment = getComment(commentId);
        if (!comment.getUserId().equals(authenticatedUserId)) {
            throw new AccessDeniedException(
                    FORBIDDEN,
                    messageService.getProperty("error.access.denied")
            );
        }
        Photo photo = photoService.getPhotoById(photoId);
        comment.setPhoto(null);
        photoService.delete(authenticatedUserId, photo);
    }

    private Comment createComment(
            Long authenticatedUserId,
            Comment comment,
            String text
    ) {
        return commentRepository.save(
                Comment.builder()
                        .createdAt(Instant.now())
                        .userId(authenticatedUserId)
                        .comment(comment)
                        .text(text)
                        .status(CommentStatus.NORMAL)
                        .build()
        );
    }

    private Comment createComment(
            Long authenticatedUserId,
            String text
    ) {
        return commentRepository.save(
                Comment.builder()
                        .createdAt(Instant.now())
                        .userId(authenticatedUserId)
                        .text(text)
                        .status(CommentStatus.NORMAL)
                        .likes(new HashSet<>())
                        .comments(new HashSet<>())
                        .build()
        );
    }
}
