package net.maslyna.post.model.dto.response;

import lombok.Builder;
import net.maslyna.post.model.PostStatus;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record PostResponse (
        UUID postId,
        Long userId,
        Instant createdAt,
        PostStatus status,
        String title,
        String text,
        UUID originalPost,
        Set<HashtagResponse> hashtags,
        Set<PhotoResponse> photos,
        long commentsAmount,
        long likesAmount
) {
}
