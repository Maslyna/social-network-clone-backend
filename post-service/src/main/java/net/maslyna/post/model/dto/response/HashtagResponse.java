package net.maslyna.post.model.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record HashtagResponse (
        UUID hashtagId,
        String text
) {
}
