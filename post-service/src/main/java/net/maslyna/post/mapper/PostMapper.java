package net.maslyna.post.mapper;

import net.maslyna.common.kafka.dto.PostCreatedEvent;
import net.maslyna.post.model.dto.response.PostResponse;
import net.maslyna.post.model.entity.post.Post;

public interface PostMapper {

    PostResponse postToPostResponse(Post post);

    PostCreatedEvent postToPostCreatedResponse(Post post);
}
