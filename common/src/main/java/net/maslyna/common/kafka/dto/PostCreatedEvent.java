package net.maslyna.common.kafka.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record PostCreatedEvent(
        Long userId,
        UUID post,
        UUID rePost,
        String title,
        Instant createdAt
) {
}
