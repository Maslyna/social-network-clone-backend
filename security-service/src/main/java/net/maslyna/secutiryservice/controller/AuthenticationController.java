package net.maslyna.secutiryservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.secutiryservice.model.dto.request.AuthenticationRequest;
import net.maslyna.secutiryservice.model.dto.request.RegistrationRequest;
import net.maslyna.secutiryservice.model.dto.response.AccountResponse;
import net.maslyna.secutiryservice.model.dto.response.AuthenticationResponse;
import net.maslyna.secutiryservice.model.entity.Account;
import net.maslyna.secutiryservice.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RequiredArgsConstructor
@Slf4j

@RestController
@RequestMapping("/api/v1/security")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registration(
            @RequestBody @Valid RegistrationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.registration(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @AuthenticationPrincipal Account account
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.authenticate(account));
    }

    @GetMapping("/validate")
    public ResponseEntity<AccountResponse> validateToken(
            @AuthenticationPrincipal Account account
    ) {
        return ResponseEntity.ok(authenticationService.getUserInfo(account));
    }
}
