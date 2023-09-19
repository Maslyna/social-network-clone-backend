package net.maslyna.secutiryservice.model.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorMessageResponse(
        String type,
        HttpStatus title,
        int statusCode,
        String message
) {
}
