package net.maslyna.common.response;

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