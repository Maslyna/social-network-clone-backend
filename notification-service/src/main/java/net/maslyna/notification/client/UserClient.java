package net.maslyna.notification.client;

import net.maslyna.common.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/api/v1/user/{userId}")
    UserResponse getUserById(@PathVariable("userId") Long userId);
}
