package net.maslyna.secutiryservice.controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.secutiryservice.model.dto.request.AuthenticationRequest;
import net.maslyna.secutiryservice.model.dto.response.AuthenticationResponse;
import net.maslyna.secutiryservice.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RequiredArgsConstructor

@RestController
@RequestMapping("/api/v1/security")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registration(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.registration(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(authenticationService.authenticate(request));
    }
}
