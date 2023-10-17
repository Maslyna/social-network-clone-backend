package net.maslyna.post.integration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;

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

    public <T> T extract(String content, Class<T> clazz) throws JsonProcessingException {
        return (T) objectMapper.readValue(content, clazz);
    }
}
