package net.maslyna.secutiryservice.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/api/v1/test")
    public String test() {
        return "test!";
    }
}
