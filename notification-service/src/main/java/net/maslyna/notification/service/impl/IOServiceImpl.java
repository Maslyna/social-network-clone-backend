package net.maslyna.notification.service.impl;

import net.maslyna.notification.service.IOService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class IOServiceImpl implements IOService {
    
    @Override public String getFileAsString(String filePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(filePath);
        Path path = resource.getFile().toPath();
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
