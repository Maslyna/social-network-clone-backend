package net.maslyna.user.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditUserAccountRequest(
        @NotBlank @Email
        String email,
        @NotBlank @Size(min = 8, max = 100)
        String password
) {
}
