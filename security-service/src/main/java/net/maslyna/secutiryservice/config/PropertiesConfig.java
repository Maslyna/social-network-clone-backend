package net.maslyna.secutiryservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:error-messages.yaml")
public class PropertiesConfig {
}
