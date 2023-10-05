package net.maslyna.user.client;

import net.maslyna.user.model.dto.request.UserRegistrationFollowerServiceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("follower-service")
public interface FollowerClient {
    @PostMapping("/api/v1/user/followers")
    ResponseEntity<Void> userRegistration(@RequestBody UserRegistrationFollowerServiceRequest request);
}
