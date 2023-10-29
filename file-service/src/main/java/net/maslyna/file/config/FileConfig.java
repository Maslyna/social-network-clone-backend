package net.maslyna.file.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Slf4j
@Configuration
@PropertySource("classpath:${envTarget:file-valid-content-config}.properties")
public class FileConfig {

    @Value("${file.content-types}")
    private List<String> fileContentTypes;

    @PostConstruct
    private void logging() {
        log.info("valid file content types = {}", fileContentTypes);
    }

    @Bean(name = "valid-content-types") //DON'T CHANGE NAME
    public List<String> validContentTypes() {
        return fileContentTypes;
    }
}