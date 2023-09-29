package net.maslyna.post.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.post.mapper.CommentMapper;
import net.maslyna.post.model.dto.request.CommentRequest;
import net.maslyna.post.service.CommentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@Validated
@Slf4j
public class CommentController {
    private final CommentMapper commentMapper;
    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    @ResponseStatus(CREATED)
    public ResponseEntity<UUID> createComment(
            @RequestHeader("userId") Long authenticatedUserId,
            @PathVariable("postId") UUID postId,
            @RequestBody CommentRequest commentRequest
    ) {
        return ResponseEntity.status(CREATED).body(
                commentService.postComment(authenticatedUserId, postId, commentRequest)
        );
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getPostComments(
            @PathVariable("postId") UUID postId,
            @RequestHeader("userId") Long authenticatedUserId,
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
                commentService.getComments(
                        authenticatedUserId,
                        postId,
                        PageRequest.of(pageNum, pageSize, Direction.valueOf(order), sortBy)
                ).map(commentMapper::commentToCommentResponse)
        );
    }

    @PostMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<UUID> createComment(
            @RequestHeader("userId") Long authenticatedUserId,
            @PathVariable("postId") UUID postId,
            @PathVariable("commentId") UUID commentId,
            @RequestBody CommentRequest commentRequest
    ) {
        return ResponseEntity.status(CREATED).body(
                commentService.postComment(authenticatedUserId, postId, commentId, commentRequest)
        );
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<UUID> editComment(
            @RequestHeader("userId") Long authenticatedUserId,
            @PathVariable("postId") UUID postId,
            @PathVariable("commentId") UUID commentId,
            @RequestBody CommentRequest commentRequest
    ) {
        return ResponseEntity.ok(
                commentService.editComment(authenticatedUserId, postId, commentId, commentRequest)
        );
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(OK)
    public void deleteComment(
            @RequestHeader("userId") Long authenticatedUserId,
            @PathVariable("postId") UUID postId,
            @PathVariable("commentId") UUID commentId
    ) {
        commentService.deleteComment(authenticatedUserId, postId, commentId);
    }
}
