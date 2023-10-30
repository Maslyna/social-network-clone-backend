package net.maslyna.secutiry.controller;

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

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registration(
            @RequestBody @Valid RegistrationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.registration(request));
    }

    @PutMapping("/edit")
    public ResponseEntity<AuthenticationResponse> edit(
            @AuthenticationPrincipal Account account,
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.edit(account, request));
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
