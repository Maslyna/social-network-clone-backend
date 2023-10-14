package net.maslyna.secutiry.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JsonService {
    private final ObjectMapper objectMapper;

    public String extract(String content, String value) throws IOException {
        return objectMapper.reader().readTree(content).get(value).asText();
    }

    public String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
