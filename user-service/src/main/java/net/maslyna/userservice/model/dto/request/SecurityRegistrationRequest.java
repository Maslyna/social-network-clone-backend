package net.maslyna.userservice.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record SecurityRegistrationRequest (
        Long id,
        @NotEmpty @Email
        String email,
        String password
) {
}
