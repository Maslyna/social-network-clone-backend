package net.maslyna.secutiryservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/v1/test")
    public ResponseEntity<String> test(
            @RequestHeader("userId") Long id,
            @RequestHeader("username") String username
    ) {
        return ResponseEntity.ok("TEST" + username + id);
    }
}
