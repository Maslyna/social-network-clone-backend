package net.maslyna.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import net.maslyna.user.mapper.UserMapper;
import net.maslyna.user.model.dto.request.EditUserRequest;
import net.maslyna.user.model.dto.request.UserRegistrationRequest;
import net.maslyna.user.model.dto.response.UserRegistrationResponse;
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

    @Operation(summary = "user registration")
    @PostMapping
    public ResponseEntity<UserRegistrationResponse> registration(
            @RequestBody @Valid UserRegistrationRequest request
    ) {
        return ResponseEntity.status(CREATED)
                .body(userService.registration(request));
    }

    @Operation(summary = "get all users")
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

    @Operation(summary = "get user info by id")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("userId") Long userId,
            @RequestHeader(required = false, name = "userId") Long authUserId
    ) {
        return ResponseEntity.status(OK)
                .body(mapper.userToUserResponse(userService.getUser(userId), authUserId));
    }

    @Operation(summary = "edit user")
    @PutMapping("/edit")
    @ResponseStatus(OK)
    public void editUserById(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody EditUserRequest userRequest
    ) {
        userService.editUser(userId, userRequest);
    }
}
