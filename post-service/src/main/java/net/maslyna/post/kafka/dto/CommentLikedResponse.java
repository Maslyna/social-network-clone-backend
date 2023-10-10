package net.maslyna.post.kafka.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CommentLikedResponse(
        UUID commentId,
        UUID postId,
        Long commentOwnerId,
        Long userId
) {
}
