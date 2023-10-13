package net.maslyna.notification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:${envTarget:notification}.properties")
public class PropertiesConfig {
}