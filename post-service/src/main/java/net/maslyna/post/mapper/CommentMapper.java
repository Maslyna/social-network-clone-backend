package net.maslyna.post.mapper;

import net.maslyna.post.model.dto.response.CommentResponse;
import net.maslyna.post.model.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "commentStatus", expression = "java(comment.getStatus())")
    @Mapping(target = "commentId", expression = "java(comment.getId())")
    CommentResponse commentToCommentResponse(Comment comment);
}
