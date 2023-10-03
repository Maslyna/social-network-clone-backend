package net.maslyna.user.model.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthenticationResponse (
        UUID token
) {
}
