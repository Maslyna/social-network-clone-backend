package net.maslyna.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.post.exception.CommentNotFoundException;
import net.maslyna.post.mapper.CommentMapper;
import net.maslyna.post.model.CommentStatus;
import net.maslyna.post.model.dto.request.CommentRequest;
import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.Post;
import net.maslyna.post.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final PropertiesMessageService messageService;

    @Transactional(readOnly = true)
    public Page<Comment> getComments(
            Long authenticatedUserId,
            UUID postId,
            PageRequest pageRequest) {
        Post post = postService.getPost(authenticatedUserId, postId);
        List<Comment> comments = new ArrayList<>(post.getComments());
        return new PageImpl<>(comments, pageRequest, comments.size());
    }

    public UUID postComment(
            Long authenticatedUserId,
            UUID postId,
            CommentRequest commentRequest) {
        Post post = postService.getPost(authenticatedUserId, postId);
        Comment comment = createComment(authenticatedUserId, commentRequest.text());
        post.addComment(comment);
        return comment.getId();
    }

    public UUID postComment(
            Long authenticatedUserId,
            UUID postId,
            UUID commentId,
            CommentRequest commentRequest) {
        Post post = postService.getPost(authenticatedUserId, postId);
        Comment comment = getComment(commentId);
        Comment newComment = createComment(authenticatedUserId, commentRequest.text());
        comment.addComment(newComment);
        return newComment.getId();
    }

    public UUID editComment(
            Long authenticatedUserId,
            UUID postId,
            UUID commentId,
            CommentRequest commentRequest) {
        Post post = postService.getPost(authenticatedUserId, postId);
        Comment comment = getComment(commentId);

        if (commentRequest.text() != null) {
            comment.setText(commentRequest.text());
            comment.setStatus(CommentStatus.EDITED);
        }
        return comment.getId();
    }

    private Comment getComment(UUID commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(NOT_FOUND,
                        messageService.getProperty("error.comment.not-found", commentId)
                ));
    }

    private Comment createComment(Long authenticatedUserId, Comment comment, String text) {
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

    private Comment createComment(Long authenticatedUserId, String text) {
        return commentRepository.save(
                Comment.builder()
                        .createdAt(Instant.now())
                        .userId(authenticatedUserId)
                        .text(text)
                        .status(CommentStatus.NORMAL)
                        .build()
        );
    }
}
