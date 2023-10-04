package net.maslyna.common.response;

import lombok.Builder;
import net.maslyna.common.message.MessageType;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@Builder
public record ErrorMessageResponse(
        Instant createdAt,
        MessageType type,
        HttpStatus statusCode,
        int status,
        String message,
        Map<String, Object> details
) {
}
