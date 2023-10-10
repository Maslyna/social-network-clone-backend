package net.maslyna.post.kafka.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PostLikedResponse (
        Long postOwnerId,
        UUID postId,
        Long userId
) {
}
