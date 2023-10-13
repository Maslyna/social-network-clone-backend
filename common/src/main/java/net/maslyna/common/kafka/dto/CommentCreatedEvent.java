package net.maslyna.common.kafka.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CommentCreatedEvent(
        Long userId,
        UUID postId,
        UUID originalCommentId,
        Long commentOwnerId,
        UUID commentId
) {
}
