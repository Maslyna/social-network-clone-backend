package net.maslyna.file.response;

import lombok.Builder;

import java.util.Map;

@Builder
public record FileStatus(
        String status,
        Map<String, Object> details
) {
}
