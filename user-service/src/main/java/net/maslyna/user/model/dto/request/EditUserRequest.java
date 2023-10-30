package net.maslyna.user.model.dto.request;

import lombok.Builder;

import java.time.Instant;

@Builder
public record EditUserRequest(
        String name,
        Instant birthday
) {
}
