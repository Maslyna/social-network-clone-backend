package net.maslyna.post.mapper;

import net.maslyna.post.model.dto.response.CommentResponse;
import net.maslyna.post.model.entity.Comment;

public interface CommentMapper {

    CommentResponse commentToCommentResponse(Comment comment);

}
