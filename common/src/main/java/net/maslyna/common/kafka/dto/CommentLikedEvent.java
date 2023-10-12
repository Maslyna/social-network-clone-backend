package net.maslyna.common.kafka.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CommentLikedEvent(
        UUID commentId,
        UUID postId,
        Long commentOwnerId,
        Long userId
) {
}
