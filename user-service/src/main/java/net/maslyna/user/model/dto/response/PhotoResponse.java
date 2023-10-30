package net.maslyna.user.model.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PhotoResponse(
        UUID photoId,
        String imageUrl
) {
}
