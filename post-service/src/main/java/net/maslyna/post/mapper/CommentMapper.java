package net.maslyna.post.mapper;

import net.maslyna.post.model.dto.response.CommentResponse;
import net.maslyna.post.model.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    //TODO: just java(comment.getComments().stream().map(this::commentToCommentResponse).collect(java.util.stream.Collectors.toSet()))
    // doesn't working

//    @Mapping(target = "userId", expression = "java(comment.getUserId())")
//    @Mapping(target = "createdAt", expression = "java(comment.getCreatedAt())")
//    @Mapping(target = "commentStatus", expression = "java(comment.getStatus())")
//    @Mapping(target = "commentId", expression = "java(comment.getId())")
//    @Mapping(target = "text", expression = "java(comment.getText())")
//    @Mapping(target = "comments",
//            expression = "java(comment.getComments().stream().map(this::commentToCommentResponse).collect(java.util.stream.Collectors.toSet()))")
//    CommentResponse commentToCommentResponse(Comment comment);

    default CommentResponse commentToCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .commentStatus(comment.getStatus())
                .userId(comment.getUserId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .comments(comment.getComments().stream().map(this::commentToCommentResponseImpl).collect(Collectors.toSet()))
                .build();
    }

    default CommentResponse commentToCommentResponseImpl(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .commentStatus(comment.getStatus())
                .userId(comment.getUserId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
