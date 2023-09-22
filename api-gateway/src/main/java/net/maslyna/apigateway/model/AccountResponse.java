package net.maslyna.apigateway.model;

import lombok.Builder;

@Builder
public record AccountResponse(
        Long id,
        String email
) {
}