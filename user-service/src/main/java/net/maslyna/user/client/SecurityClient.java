package net.maslyna.user.client;

import net.maslyna.user.model.dto.request.SecurityRegistrationRequest;
import net.maslyna.user.model.dto.response.AuthenticationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("security-service")
public interface SecurityClient {
    @PostMapping("/api/v1/security/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody SecurityRegistrationRequest request
    );
}
