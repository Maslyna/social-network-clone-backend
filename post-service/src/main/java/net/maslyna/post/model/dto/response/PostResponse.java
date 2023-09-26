package net.maslyna.post.model.dto.response;

import net.maslyna.post.model.PostStatus;

import java.util.UUID;

public record PostResponse (
        UUID postId,
        Long userId,
        PostStatus status,
        String title,
        String text

) {
}
