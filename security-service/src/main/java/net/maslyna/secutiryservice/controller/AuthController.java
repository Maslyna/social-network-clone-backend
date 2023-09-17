package net.maslyna.secutiryservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.maslyna.secutiryservice.model.dto.request.AccountRegistrationRequest;
import net.maslyna.secutiryservice.service.AuthService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/security")
@Validated
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping
    public Boolean registration(@RequestBody @Valid AccountRegistrationRequest request) {
        return authService.registration(request.email(), request.password());
    }
}
