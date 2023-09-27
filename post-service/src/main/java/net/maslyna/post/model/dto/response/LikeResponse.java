package net.maslyna.post.model.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record LikeResponse(
        UUID likeId,
        Long userId,
        Instant createdAt
) {
}
