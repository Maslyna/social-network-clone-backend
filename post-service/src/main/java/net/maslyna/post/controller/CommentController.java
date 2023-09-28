package net.maslyna.post.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import net.maslyna.post.mapper.CommentMapper;
import net.maslyna.post.model.dto.request.CommentRequest;
import net.maslyna.post.service.CommentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentMapper commentMapper;
    private final CommentService commentService;

    @PostMapping("/{postId}")
    public void createComment(
            @PathVariable("postId") UUID postId,
            @RequestHeader("userId") Long authenticatedUserId,
            @RequestBody CommentRequest commentRequest
    ) {
        commentService.postComment(authenticatedUserId, postId, commentRequest);
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
                    message = "'direction' query param must be equals ASC or DESC"
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
}
