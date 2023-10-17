package net.maslyna.post.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import net.maslyna.post.model.PostStatus;

import java.util.Set;

@Builder
public record PostRequest (
        @NotBlank
        @Size(max = 100, min = 8)
        String title,
        @NotBlank
        @Size(max = 500)
        String text,
        Set<String> hashtags,
        @NotNull
        PostStatus status
) {
}
