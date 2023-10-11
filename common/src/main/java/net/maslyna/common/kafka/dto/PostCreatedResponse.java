package net.maslyna.common.kafka.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record PostCreatedResponse(
        Long userId,
        UUID rePost,
        String title,
        Instant createdAt
) {
}
