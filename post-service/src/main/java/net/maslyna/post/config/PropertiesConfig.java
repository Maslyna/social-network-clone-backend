package net.maslyna.post.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:${envTarget:error-messages}.properties")
public class PropertiesConfig {
}