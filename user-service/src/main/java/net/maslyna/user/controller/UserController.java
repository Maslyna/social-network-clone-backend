package net.maslyna.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import net.maslyna.user.mapper.UserMapper;
import net.maslyna.user.model.dto.request.EditUserRequest;
import net.maslyna.user.model.dto.request.UserRegistrationRequest;
import net.maslyna.user.model.dto.response.AuthenticationResponse;
import net.maslyna.user.model.dto.response.UserResponse;
import net.maslyna.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> registration(
            @RequestBody @Valid UserRegistrationRequest request
    ) {
        return ResponseEntity.status(CREATED)
                .body(userService.registration(request));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(value = "page", defaultValue = "0")
            @PositiveOrZero
            Integer page,
            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1)
            @Max(value = 1000)
            Integer size
    ) {
        return ResponseEntity.status(OK)
                .body(userService.getUsers(page, size)
                        .map(mapper::userToUserResponse));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.status(OK)
                .body(mapper.userToUserResponse(userService.getUser(userId)));
    }

    @PutMapping("/edit")
    @ResponseStatus(OK)
    public void editUserById(
            @RequestHeader("userId") Long userId,
            @RequestBody EditUserRequest userRequest
    ) {
        userService.editUser(userId, userRequest);
    }
}
