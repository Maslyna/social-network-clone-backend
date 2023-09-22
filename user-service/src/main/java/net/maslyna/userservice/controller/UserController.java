package net.maslyna.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.maslyna.userservice.model.dto.request.UserRegistrationRequest;
import net.maslyna.userservice.model.dto.response.AuthenticationResponse;
import net.maslyna.userservice.model.dto.response.UserResponse;
import net.maslyna.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> registration(
            @RequestBody @Valid UserRegistrationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registration(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok().build(); //TODO: getUser
    }
}
