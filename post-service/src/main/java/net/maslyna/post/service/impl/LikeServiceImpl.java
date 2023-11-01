package net.maslyna.post.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.post.exception.LikeAlreadyExists;
import net.maslyna.post.exception.NotFoundException;
import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.model.entity.like.CommentLike;
import net.maslyna.post.model.entity.like.PostLike;
import net.maslyna.post.model.entity.post.Post;
import net.maslyna.post.producer.KafkaProducer;
import net.maslyna.post.repository.CommentLikeRepository;
import net.maslyna.post.repository.LikeRepository;
import net.maslyna.post.repository.PostLikeRepository;
import net.maslyna.post.service.CommentService;
import net.maslyna.post.service.LikeService;
import net.maslyna.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LikeServiceImpl implements LikeService {
    private final PostService postService;
    private final CommentService commentService;
    //TODO: separate likes repo logic in different service
    private final LikeRepository likeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostLikeRepository postLikeRepository;
    private final PropertiesMessageService messageService;
    private final KafkaProducer kafkaProducer;

    @Override
    public UUID likePost(Long authenticatedUserId, UUID postId) {
        if (isLikeOnPostAlreadyExist(authenticatedUserId, postId)) {
            throw new LikeAlreadyExists(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.like.on-post-already-exists", postId)
            );
        }
        Post post = postService.getPost(authenticatedUserId, postId);
        PostLike like = createLike(authenticatedUserId, post);
        post.addLike(like);

        kafkaProducer.sendPostLikedEvent(authenticatedUserId, post);

        return like.getId();
    }

    @Override
    public UUID likeComment(Long authenticatedUserId, UUID commentId) {
        if (isLikeOnCommentAlreadyExist(authenticatedUserId, commentId)) {
            throw new LikeAlreadyExists(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.like.on-comment-already-exists", commentId)
            );
        }
        Comment comment = commentService.getComment(commentId);
        CommentLike like = createLike(authenticatedUserId, comment);
        comment.addLike(like);

        kafkaProducer.sendCommentLikedEvent(authenticatedUserId, comment);

        return like.getId();
    }

    @Override
    public void deleteLikeFromPost(Long authenticatedUserId, UUID postId) {
        PostLike like = getLikeByUserIdAndPostId(authenticatedUserId, postId);
        likeRepository.deleteLike(like);
    }

    @Override
    public void deleteLikeFromComment(Long authenticatedUserId, UUID commentId) {
        CommentLike like = getLikeByUserIdAndCommentId(authenticatedUserId, commentId);
        likeRepository.deleteLike(like);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostLike> getLikesOnPost(
            UUID postId,
            Long authenticatedUserId,
            PageRequest pageRequest) {
        Post post = postService.getPost(authenticatedUserId, postId);
        List<PostLike> likes = List.copyOf(post.getLikes());
        return new PageImpl<>(likes, pageRequest, likes.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentLike> getLikesOnComment(
            UUID commentId,
            PageRequest pageRequest) {
        Comment comment = commentService.getComment(commentId);
        List<CommentLike> likes = List.copyOf(comment.getLikes());
        return new PageImpl<>(likes, pageRequest, likes.size());
    }

    private PostLike getLikeByUserIdAndPostId(Long authenticatedUserId, UUID postId) {
        return postLikeRepository.findLikeByUserIaAndPostId(authenticatedUserId, postId)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND,
                        messageService.getProperty("error.like.on-post-not-found", postId)
                ));
    }

    private CommentLike getLikeByUserIdAndCommentId(Long authenticatedUserId, UUID commentId) {
        return commentLikeRepository.findLikeByUserIdAndCommentId(authenticatedUserId, commentId)
                .orElseThrow(() -> new NotFoundException(
                        HttpStatus.NOT_FOUND,
                        messageService.getProperty("error.like.on-comment-not-found", commentId)
                ));
    }

    private boolean isLikeOnPostAlreadyExist(Long authenticatedUserId, UUID postId) {
        return postLikeRepository.exitstByUserIdAndPostId(authenticatedUserId, postId);
    }

    private boolean isLikeOnCommentAlreadyExist(Long authenticatedUserId, UUID commentId) {
        return commentLikeRepository.existsByUserIdAndCommentId(authenticatedUserId, commentId);
    }

    private PostLike createLike(Long authenticatedUserId, Post post) {
        return postLikeRepository.save(
                PostLike.builder()
                        .post(post)
                        .userId(authenticatedUserId)
                        .createdAt(Instant.now())
                        .build()
        );
    }

    private CommentLike createLike(Long authenticatedUserId, Comment comment) {
        return commentLikeRepository.save(
                CommentLike.builder()
                        .comment(comment)
                        .userId(authenticatedUserId)
                        .createdAt(Instant.now())
                        .build()
        );
    }
}
