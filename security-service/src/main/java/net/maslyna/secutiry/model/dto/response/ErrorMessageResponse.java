package net.maslyna.secutiry.model.dto.response;

import lombok.Builder;
import net.maslyna.secutiry.model.dto.MessageType;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Builder
public record ErrorMessageResponse(
        Instant createdAd,
        MessageType type,
        HttpStatus statusCode,
        int status,
        String message
) {
}
