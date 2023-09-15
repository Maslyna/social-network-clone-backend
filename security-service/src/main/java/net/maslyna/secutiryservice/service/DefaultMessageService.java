package net.maslyna.secutiryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultMessageService {

    private final Environment environment;

    public String getProperty(String source) {
        return environment.getProperty(source);
    }

    public String getProperty(String name, Object... params) {
        String property = environment.getProperty(name);
        if (property == null) {
            throw new RuntimeException("property not found: " + name);
        }
        return property.formatted(params);
    }
}