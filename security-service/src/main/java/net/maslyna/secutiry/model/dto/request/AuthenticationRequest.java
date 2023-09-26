package net.maslyna.secutiry.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record AuthenticationRequest(
        @NotEmpty @Email
        String email,
        String password
) {
}
