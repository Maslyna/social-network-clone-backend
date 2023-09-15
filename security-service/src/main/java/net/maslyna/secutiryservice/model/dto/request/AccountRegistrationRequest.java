package net.maslyna.secutiryservice.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record AccountRegistrationRequest(
        @NotEmpty @Email
        String email,
        @Size(min=8)
        String password
) {
}
