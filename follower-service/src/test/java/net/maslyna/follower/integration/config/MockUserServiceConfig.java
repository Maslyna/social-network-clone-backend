package net.maslyna.follower.integration.config;

import net.maslyna.common.response.UserResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Configuration
@RestController
@ActiveProfiles("test")
public class MockUserServiceConfig {
    @GetMapping("/api/v1/user/{userId}")
    UserResponse getUserById(@PathVariable("userId") Long userId) {
        return UserResponse.builder()
                .id(userId)
                .email("test@mail.net")
                .name("test-name")
                .createdAt(Instant.now())
                .build();
    }
}