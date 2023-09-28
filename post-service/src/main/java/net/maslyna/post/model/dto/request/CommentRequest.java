package net.maslyna.post.model.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CommentRequest(
        String text
) {
}
