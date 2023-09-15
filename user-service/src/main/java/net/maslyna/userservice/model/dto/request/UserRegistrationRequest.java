package net.maslyna.userservice.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record UserRegistrationRequest(
        @NotEmpty @Email(message = "{validation.email.not-valid}") String email,
        @NotEmpty String password
) {
}
