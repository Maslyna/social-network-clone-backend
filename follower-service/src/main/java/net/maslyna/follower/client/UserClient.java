package net.maslyna.follower.client;

import net.maslyna.common.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// param 'url' - used only in tests
@FeignClient(value = "${eureka.client.user-service:user-service}", url = "${eureka.client.user-service-url:''}")
public interface UserClient {
    @GetMapping("/api/v1/user/{userId}")
    UserResponse getUserById(@PathVariable("userId") Long userId);
}
