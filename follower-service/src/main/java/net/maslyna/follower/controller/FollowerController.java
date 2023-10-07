package net.maslyna.follower.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import net.maslyna.follower.mapper.UserMapper;
import net.maslyna.follower.model.dto.request.UserRegistrationRequest;
import net.maslyna.follower.model.entity.User;
import net.maslyna.follower.service.FollowerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FollowerController {
    private final FollowerService followerService;

    @PostMapping("/user/{userId}/followers")
    @ResponseStatus(OK)
    public void follow(
            @PathVariable("userId") Long userId,
            @RequestHeader("userId") Long authUserId
    ) {
        followerService.follow(authUserId, userId);
    }

    @DeleteMapping("/user/{userId}/followers")
    @ResponseStatus(OK)
    public void unfollow(
            @PathVariable("userId") Long userId,
            @RequestHeader("userId") Long authUserId
    ) {
        followerService.unfollow(authUserId, userId);
    }

    @GetMapping("/user/followers")
    public ResponseEntity<?> getFollowers(
            @RequestHeader("userId") Long userId,
            @RequestParam(value = "size", defaultValue = "5") @Min(1) @Max(1000) Integer pageSize,
            @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero Integer pageNum,
            @RequestParam(value = "orderBy", defaultValue = "DESC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "error.validation.sort.direction.message"
            )
            String order,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String... sortBy
    ) {
        return ResponseEntity.ok(
                followerService.getUserFollowers(
                        userId,
                        PageRequest.of(pageNum, pageSize, Sort.Direction.fromString(order), sortBy)
                ).map(User::getId)
        );
    }

    @GetMapping("/user/{userId}/followers")
    public ResponseEntity<?> getFollowers(
            @PathVariable("userId") Long userId,
            @RequestHeader("userId") Long authUserId,
            @RequestParam(value = "size", defaultValue = "5") @Min(1) @Max(1000) Integer pageSize,
            @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero Integer pageNum,
            @RequestParam(value = "orderBy", defaultValue = "DESC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "error.validation.sort.direction.message"
            )
            String order,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String... sortBy
    ) {
        return ResponseEntity.ok(
                followerService.getUserFollowers(
                        authUserId,
                        userId,
                        PageRequest.of(pageNum, pageSize, Sort.Direction.fromString(order), sortBy)
                ).map(User::getId)
        );
    }

    @GetMapping("/user/subscriptions")
    public ResponseEntity<?> getSubscriptions(
            @RequestHeader("userId") Long userId,
            @RequestParam(value = "size", defaultValue = "5") @Min(1) @Max(1000) Integer pageSize,
            @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero Integer pageNum,
            @RequestParam(value = "orderBy", defaultValue = "DESC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "error.validation.sort.direction.message"
            )
            String order,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String... sortBy
    ) {
        return ResponseEntity.ok(
                followerService.getUserSubscriptions(
                        userId,
                        PageRequest.of(pageNum, pageSize, Sort.Direction.fromString(order), sortBy)
                ).map(User::getId)
        );
    }

    @GetMapping("/user/{userId}/subscriptions")
    public ResponseEntity<?> getSubscriptions(
            @PathVariable("userId") Long userId,
            @RequestHeader("userId") Long authUserId,
            @RequestParam(value = "size", defaultValue = "5") @Min(1) @Max(1000) Integer pageSize,
            @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero Integer pageNum,
            @RequestParam(value = "orderBy", defaultValue = "DESC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "error.validation.sort.direction.message"
            )
            String order,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String... sortBy
    ) {
        return ResponseEntity.ok(
                followerService.getUserSubscriptions(
                        authUserId,
                        userId,
                        PageRequest.of(pageNum, pageSize, Sort.Direction.fromString(order), sortBy)
                ).map(User::getId)
        );
    }
}
