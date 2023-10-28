package net.maslyna.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource("classpath:${envTarget:file-valid-content-config}.properties")
public class FileConfig {

    @Value("${file.content-types}")
    private List<String> fileContentTypes;

    @Bean(name = "valid-content-types") //DON'T CHANGE NAME
    public List<String> validContentTypes() {
        return fileContentTypes;
    }
}