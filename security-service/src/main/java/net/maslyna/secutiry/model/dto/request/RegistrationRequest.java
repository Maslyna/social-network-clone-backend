package net.maslyna.secutiry.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegistrationRequest (
        @NotNull
        Long id,
        @NotEmpty @Email
        String email,
        @Size(min = 8)
        String password
) {
}
