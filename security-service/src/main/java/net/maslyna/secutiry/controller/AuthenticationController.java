package net.maslyna.secutiry.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.secutiry.model.dto.request.AuthenticationRequest;
import net.maslyna.secutiry.model.dto.request.RegistrationRequest;
import net.maslyna.secutiry.model.dto.response.AccountResponse;
import net.maslyna.secutiry.model.dto.response.AuthenticationResponse;
import net.maslyna.secutiry.model.entity.Account;
import net.maslyna.secutiry.service.AuthenticationService;
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

    @Operation(summary = "new user registration")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registration(
            @RequestBody @Valid RegistrationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.registration(request));
    }

    @Operation(summary = "edit user")
    @PutMapping("/edit")
    public ResponseEntity<AuthenticationResponse> edit(
            @AuthenticationPrincipal Account account,
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.edit(account, request));
    }

    @Operation(summary = "generate new bearer token")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @AuthenticationPrincipal Account account
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.authenticate(account));
    }

    @Operation(summary = "validate bearer token")
    @GetMapping("/validate")
    public ResponseEntity<AccountResponse> validateToken(
            @AuthenticationPrincipal Account account
    ) {
        return ResponseEntity.ok(authenticationService.getUserInfo(account));
    }
}
