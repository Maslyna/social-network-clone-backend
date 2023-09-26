package net.maslyna.user.model.dto.response;

import lombok.Builder;

@Builder
public record AuthenticationResponse (
        String token
) {
}
