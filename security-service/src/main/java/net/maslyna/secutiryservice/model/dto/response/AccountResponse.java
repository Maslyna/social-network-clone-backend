package net.maslyna.secutiryservice.model.dto.response;

import lombok.Builder;

@Builder
public record AccountResponse(
        long accountId,
        String email
) {
}
