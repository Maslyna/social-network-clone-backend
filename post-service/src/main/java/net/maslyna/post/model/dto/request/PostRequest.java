package net.maslyna.post.model.dto.request;

import net.maslyna.post.model.PostStatus;

import java.util.Set;

public record PostRequest (
        String title,
        String text,
        Set<String> hashtags,
        PostStatus status
) {
}
