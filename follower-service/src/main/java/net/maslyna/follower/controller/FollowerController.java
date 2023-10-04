package net.maslyna.follower.controller;

import lombok.RequiredArgsConstructor;
import net.maslyna.follower.model.dto.request.UserRegistrationRequest;
import net.maslyna.follower.service.FollowerService;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/follower")
@RequiredArgsConstructor
public class FollowerController {
    private final FollowerService followerService;

    @PostMapping
    @ResponseStatus(CREATED)
    private void userRegistration(
            @RequestBody UserRegistrationRequest request
    ) {
        followerService.userRegistration(request.userId());
    }
}
