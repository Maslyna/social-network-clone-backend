package net.maslyna.post.model.dto.response;

import lombok.Builder;
import net.maslyna.post.model.CommentStatus;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record CommentResponse(
        UUID commentId,
        Long userId,
        String text,
        CommentStatus commentStatus,
        Set<CommentResponse> comments,
        long likes,
        Instant createdAt
) {
}
