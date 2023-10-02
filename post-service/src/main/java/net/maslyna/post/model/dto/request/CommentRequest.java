package net.maslyna.post.model.dto.request;

import lombok.Builder;

@Builder
public record CommentRequest(
        String text
) {
}
