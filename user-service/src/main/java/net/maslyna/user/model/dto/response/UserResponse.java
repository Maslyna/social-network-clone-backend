package net.maslyna.user.model.dto.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record UserResponse(
        Long id,
        String name,
        String nickname,
        String email,
        String bio,
        String location,
        Instant createdAt,
        Instant birthday,
        String imageUrl,
        boolean isPublicAccount) {
}
