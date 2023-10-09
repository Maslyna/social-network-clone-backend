package net.maslyna.post.kafka.dto;

import lombok.Builder;
import net.maslyna.post.model.dto.response.HashtagResponse;

import java.util.List;

@Builder
public record PostCreatedResponse(
        Long userId,
        String title,
        String text,
        List<HashtagResponse> hashtags
) {
}
