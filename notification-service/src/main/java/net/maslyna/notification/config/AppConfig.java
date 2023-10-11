package net.maslyna.notification.config;

import net.maslyna.common.service.PropertiesMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
    @Bean
    PropertiesMessageService messageService(Environment env) {
        return new PropertiesMessageService(env);
    }
}
