package net.maslyna.secutiryservice.model.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token
) {
}
