package net.maslyna.post.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import net.maslyna.post.model.dto.request.PostRequest;
import net.maslyna.post.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Validated
public class PostController {
    private final PostService postService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void createPost(
            @RequestHeader(name = "userId") Long userId,
            @RequestBody PostRequest request
    ) {
        postService.createPost(userId, request);
    }

    @GetMapping
    public ResponseEntity<?> getPublicPosts(
            @RequestParam(value = "size", defaultValue = "5") @Min(1) @Max(1000) Integer pageSize,
            @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero Integer pageNum,
            @RequestParam(value = "orderBy", defaultValue = "ASC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "'direction' query param must be equals ASC or DESC"
            )
            String order,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String... sortBy
    ) {
        return ResponseEntity.ok( //TODO: mapper
                postService.getAllPosts(PageRequest.of(pageNum, pageSize, Direction.fromString(order), sortBy))
        );
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<?> getPosts(
//            @RequestHeader
//    )
}
