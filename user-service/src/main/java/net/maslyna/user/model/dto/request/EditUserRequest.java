package net.maslyna.user.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.Instant;

@Builder
public record EditUserRequest(
        @Email
        @Size(max = 100)
        String email,
        @Size(max = 100)
        String name,
        @Size(max = 100)
        String nickname,
        @Size(max = 1000)
        String bio,
        @Size(max = 200)
        String location,
        Boolean isPublicAccount,
        Instant birthday
) {
}
