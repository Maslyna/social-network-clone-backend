package net.maslyna.post.controller;

import lombok.RequiredArgsConstructor;
import net.maslyna.post.model.dto.request.PostRequest;
import net.maslyna.post.service.PostService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
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
}
