package net.maslyna.post.model.dto.response;

import lombok.Builder;
import net.maslyna.post.model.PostStatus;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record FullPostResponse (
        UUID postId,
        Long userId,
        Instant createdAt,
        PostStatus status,
        String title,
        String text,
        Set<HashtagResponse> hashtags,
        long commentsAmount,
        long likesAmount
) {
}
