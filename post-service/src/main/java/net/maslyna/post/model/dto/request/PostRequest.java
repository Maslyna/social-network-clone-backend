package net.maslyna.post.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import net.maslyna.post.model.PostStatus;

import java.util.Set;

public record PostRequest (
        @NotBlank
        String title,
        @NotBlank
        String text,
        Set<String> hashtags,
        @NotNull
        PostStatus status
) {
}
