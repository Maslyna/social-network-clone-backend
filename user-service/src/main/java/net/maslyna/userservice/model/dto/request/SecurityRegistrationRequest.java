package net.maslyna.userservice.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SecurityRegistrationRequest(
        @NotNull
        Long id,
        @NotEmpty @Email
        String email,
        String password
) {
}
