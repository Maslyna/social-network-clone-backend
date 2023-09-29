package net.maslyna.post.mapper;

import net.maslyna.post.model.dto.response.CommentResponse;
import net.maslyna.post.model.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "userId", expression = "java(comment.getUserId())")
    @Mapping(target = "createdAt", expression = "java(comment.getCreatedAt())")
    @Mapping(target = "commentStatus", expression = "java(comment.getStatus())")
    @Mapping(target = "commentId", expression = "java(comment.getId())")
    @Mapping(target = "text", expression = "java(comment.getText())")
    @Mapping(target = "comments",
            expression = "java(comment.getComments().stream().map(this::commentToCommentResponse).collect(java.util.stream.Collectors.toSet()))")
    CommentResponse commentToCommentResponse(Comment comment);

}
