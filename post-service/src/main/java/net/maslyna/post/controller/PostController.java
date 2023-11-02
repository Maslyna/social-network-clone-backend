package net.maslyna.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import net.maslyna.post.mapper.PostMapper;
import net.maslyna.post.model.dto.request.PostRequest;
import net.maslyna.post.model.dto.response.PostResponse;
import net.maslyna.post.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Validated
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    @Operation(description = "create new post")
    @PostMapping("/create")
    public ResponseEntity<UUID> createPost(
            @RequestHeader(name = "userId") Long userId,
            @Valid @RequestBody PostRequest request
    ) {
        return ResponseEntity.status(CREATED).body(
                postService.createPost(userId, request)
        );
    }

    @Operation(description = "create new repost")
    @PostMapping("/{postId}/repost")
    public ResponseEntity<UUID> createRepost(
            @RequestHeader("userId") Long userId,
            @PathVariable("postId") UUID postId,
            @Valid @RequestBody PostRequest request
    ) {
        return ResponseEntity.status(CREATED).body(
                postService.createRepost(userId, postId, request)
        );
    }

    @Operation(description = "edit post")
    @PutMapping("/{postId}")
    public ResponseEntity<UUID> editPost(
            @RequestHeader(name = "userId") Long authenticatedUserId,
            @PathVariable(name = "postId") UUID postId,
            @Valid @RequestBody PostRequest request
    ) {
        return ResponseEntity.status(OK).body(
                postService.editPost(authenticatedUserId, postId, request)
        );
    }

    @Operation(description = "add photo on post")
    @PostMapping("/{postId}/photo")
    public ResponseEntity<String> addPhoto(
            @RequestHeader("userId") Long authenticatedUserId,
            @PathVariable("postId") UUID postId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(
                postService.uploadPhoto(authenticatedUserId, postId, file)
        );
    }

    @Operation(description = "delete photo from post")
    @DeleteMapping("/{postId}/photo/{photoId}")
    @ResponseStatus(OK)
    public void deletePhoto(
            @RequestHeader("userId") Long authenticatedUserId,
            @PathVariable("postId") UUID postId,
            @PathVariable("photoId") UUID photoId
    ) {
        postService.deletePhoto(authenticatedUserId, postId, photoId);
    }

    @Operation(description = "get all published posts")
    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPublicPosts(
            @RequestParam(value = "size", defaultValue = "5") @Min(1) @Max(1000) Integer pageSize,
            @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero Integer pageNum,
            @RequestParam(value = "orderBy", defaultValue = "DESC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "error.validation.sort.direction.message"
            )
            String order,
            @RequestParam(value = "hashtag", required = false) String[] hashtags,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String... sortBy
    ) {
        return ResponseEntity.ok(
                postService.getPosts(
                        hashtags,
                        PageRequest.of(pageNum, pageSize, Direction.fromString(order), sortBy)
                ).map(postMapper::postToPostResponse)
        );
    }

    @Operation(description = "get person's posts")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostResponse>> getPersonPosts(
            @RequestHeader(name = "userId") Long authenticatedUserId,
            @PathVariable("userId") Long userId,
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
                postService.getPersonPosts(
                        userId,
                        authenticatedUserId,
                        PageRequest.of(pageNum, pageSize, Direction.fromString(order), sortBy)
                ).map(postMapper::postToPostResponse)
        );
    }

    @Operation(description = "get post")
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable("postId") UUID postId,
            @RequestHeader("userId") Long authenticatedUserId
    ) {
        return ResponseEntity.ok(
                postMapper.postToPostResponse(postService.getPost(authenticatedUserId, postId))
        );
    }

    @Operation(description = "delete post")
    @DeleteMapping("/{postId}")
    @ResponseStatus(OK)
    public void deletePost(
            @PathVariable("postId") UUID postId,
            @RequestHeader("userId") Long authenticatedUserId
    ) {
        postService.deletePost(authenticatedUserId, postId);
    }
}
