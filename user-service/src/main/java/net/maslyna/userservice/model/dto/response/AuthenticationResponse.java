package net.maslyna.userservice.model.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse (
        String token
) {
}
