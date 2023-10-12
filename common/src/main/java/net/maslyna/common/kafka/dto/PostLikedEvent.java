package net.maslyna.common.kafka.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PostLikedEvent(
        Long postOwnerId,
        UUID postId,
        Long userId
) {
}
