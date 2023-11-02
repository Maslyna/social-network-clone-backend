package net.maslyna.post.mapper.impl;

import lombok.RequiredArgsConstructor;
import net.maslyna.post.mapper.CommentMapper;
import net.maslyna.post.model.dto.response.CommentResponse;
import net.maslyna.post.model.entity.Comment;
import net.maslyna.post.repository.CommentLikeRepository;
import net.maslyna.post.repository.CommentRepository;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapperImpl implements CommentMapper {
    private final CommentLikeRepository likeRepository;

    @Override
    public CommentResponse commentToCommentResponse(Comment comment) {
        if (comment == null) {
            return null;
        }

        return CommentResponse.builder()
                .commentId(comment.getId())
                .comments(setCommentsToSetCommentResponses(comment.getComments()))
                .commentStatus(comment.getStatus())
                .createdAt(comment.getCreatedAt())
                .likes(likeRepository.countLikesOnComment(comment.getId()))
                .text(comment.getText())
                .build();
    }

    private Set<CommentResponse> setCommentsToSetCommentResponses(Set<Comment> comments) {
        return comments != null
                ? comments.stream().map(this::commentToCommentResponse).collect(Collectors.toSet())
                : null;
    }
}
