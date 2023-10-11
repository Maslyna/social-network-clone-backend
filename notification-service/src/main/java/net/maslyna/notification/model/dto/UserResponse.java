package net.maslyna.notification.model.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record UserResponse(
        Long id,
        String name,
        String email,
        Instant createdAt,
        Instant birthday,
        String imageUrl
) {
}