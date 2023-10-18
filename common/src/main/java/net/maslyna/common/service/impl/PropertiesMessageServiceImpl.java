package net.maslyna.common.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.service.PropertiesMessageService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PropertiesMessageServiceImpl implements PropertiesMessageService {

    private final Environment environment;

    @Override public String getProperty(String source) {
        return environment.getProperty(source);
    }

    @Override public String getProperty(String name, Object... params) {
        String property = environment.getProperty(name);
        if (property == null) {
            return null;
        }
        return property.formatted(params);
    }
}