package net.maslyna.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertiesMessageService {

    private final Environment environment;

    public String getProperty(String source) {
        return environment.getProperty(source);
    }

    public String getProperty(String name, Object... params) {
        String property = environment.getProperty(name);
        if (property == null) return null;
        return property.formatted(params);
    }
}