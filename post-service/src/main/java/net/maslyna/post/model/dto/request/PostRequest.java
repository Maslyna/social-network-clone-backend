package net.maslyna.post.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import net.maslyna.post.model.PostStatus;

import java.util.Set;

public record PostRequest (
        @NotBlank
        @Size(max = 100, min = 20)
        String title,
        @NotBlank
        @Size(max = 500)
        String text,
        Set<String> hashtags,
        @NotNull
        PostStatus status
) {
}
