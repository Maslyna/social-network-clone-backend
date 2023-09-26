package net.maslyna.secutiry.model.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token
) {
}
