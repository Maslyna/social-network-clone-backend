package net.maslyna.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import net.maslyna.post.mapper.LikeMapper;
import net.maslyna.post.model.dto.response.LikeResponse;
import net.maslyna.post.service.LikeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.http.HttpStatus.OK;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class LikeController {
    private final LikeService likeService;
    private final LikeMapper likeMapper;

    @Operation(description = "like post")
    @PostMapping("/{postId}/like")
    public ResponseEntity<UUID> likePost(
            @RequestHeader("userId") Long authenticatedUserId,
            @PathVariable("postId") UUID postId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(likeService.likePost(authenticatedUserId, postId));
    }

    @Operation(description = "like comment")
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<UUID> likeComment(
            @RequestHeader("userId") Long authenticatedUserId,
            @PathVariable("commentId") UUID commentId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(likeService.likeComment(authenticatedUserId, commentId));
    }

    @Operation(description = "get likes on comment")
    @GetMapping("/comments/{commentId}/like")
    public ResponseEntity<Page<LikeResponse>> getLikesOnComment(
            @PathVariable("commentId") UUID commentId,
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
                likeService.getLikesOnComment(
                        commentId,
                        PageRequest.of(pageNum, pageSize, Direction.fromString(order), sortBy)
                ).map(likeMapper::likeToLikeResponse)
        );
    }

    @Operation(description = "get likes on post")
    @GetMapping("/{postId}/like")
    public ResponseEntity<Page<LikeResponse>> getLikesOnPost(
            @PathVariable("postId") UUID postId,
            @RequestHeader(name = "userId", required = false) Long authenticatedUserId,
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
                likeService.getLikesOnPost(
                        postId,
                        authenticatedUserId,
                        PageRequest.of(pageNum, pageSize, Direction.fromString(order), sortBy)
                ).map(likeMapper::likeToLikeResponse)
        );
    }


    @DeleteMapping("/{postId}/like")
    @ResponseStatus(OK)
    public void deleteLikeOnPost(
            @PathVariable("postId") UUID postId,
            @RequestHeader("userId") Long authenticatedUserId
    ) {
        likeService.deleteLikeFromPost(authenticatedUserId, postId);
    }

    @DeleteMapping("/comments/{commentId}/like")
    @ResponseStatus(OK)
    public void deleteLikeOnComment(
            @PathVariable("commentId") UUID commentId,
            @RequestHeader("userId") Long authenticatedUserId
    ) {
        likeService.deleteLikeFromComment(authenticatedUserId, commentId);
    }
}
