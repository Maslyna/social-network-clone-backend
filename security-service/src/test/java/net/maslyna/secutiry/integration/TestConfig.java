package net.maslyna.secutiry.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class TestConfig {
    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
