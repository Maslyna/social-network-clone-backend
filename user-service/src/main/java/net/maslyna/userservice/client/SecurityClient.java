package net.maslyna.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient
public interface SecurityClient {
    @PostMapping("/api/v1/security")

}
