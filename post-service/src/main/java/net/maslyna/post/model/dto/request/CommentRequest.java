package net.maslyna.post.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CommentRequest(
        @NotBlank String text
) {
}
