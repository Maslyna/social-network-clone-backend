package net.maslyna.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.post.model.CommentStatus;
import net.maslyna.post.model.dto.request.CommentRequest;
import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.Post;
import net.maslyna.post.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    @Transactional(readOnly = true)
    public Page<Comment> getComments(
            Long authenticatedUserId,
            UUID postId,
            PageRequest pageRequest) {
        Post post = postService.getPost(authenticatedUserId, postId);
        return commentRepository.findByPost(post, pageRequest);
    }

    public void postComment(
            Long authenticatedUserId,
            UUID postId,
            CommentRequest commentRequest) {
        Post post = postService.getPost(authenticatedUserId, postId);
        post.addComment(createComment(authenticatedUserId, commentRequest.text()));
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
