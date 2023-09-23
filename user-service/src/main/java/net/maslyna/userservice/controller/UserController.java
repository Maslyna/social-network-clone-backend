package net.maslyna.userservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import net.maslyna.userservice.model.dto.request.UserRegistrationRequest;
import net.maslyna.userservice.model.dto.response.AuthenticationResponse;
import net.maslyna.userservice.model.dto.response.UserResponse;
import net.maslyna.userservice.service.UserService;
import org.springframework.data.domain.Page;
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

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0")
            @PositiveOrZero(message = "validation.properties.positive-or-zero")
            Integer page,
            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "validation.properties.size")
            @Max(value = 1000)
            Integer size
    ) {
        return null;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUser(userId)); //TODO: getUser
    }
}
