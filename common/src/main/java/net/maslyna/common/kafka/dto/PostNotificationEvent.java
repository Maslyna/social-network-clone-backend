package net.maslyna.common.kafka.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PostNotificationEvent (
        List<String> emails,
        PostCreatedEvent postInfo
) {
}
