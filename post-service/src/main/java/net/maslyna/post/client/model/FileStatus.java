package net.maslyna.post.client.model;

import lombok.Builder;

import java.util.Map;

@Builder
public record FileStatus(
        String status,
        Map<String, Object> details
) {
}
