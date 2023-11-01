package net.maslyna.post.integration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@TestComponent
@RequiredArgsConstructor
@ActiveProfiles("test")
public class JsonService {
    private final ObjectMapper objectMapper;

    public String extract(String content, String value) throws IOException {
        return objectMapper.reader().readTree(content).get(value).asText();
    }

    public String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    public <T> T extract(String content, Class<T> clazz) throws JsonProcessingException {
        return (T) objectMapper.readValue(content, clazz);
    }
}
