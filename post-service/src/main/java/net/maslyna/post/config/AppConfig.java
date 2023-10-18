package net.maslyna.post.config;

import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.common.service.impl.PropertiesMessageServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {
    @Bean
    PropertiesMessageService messageService(Environment env) {
        return new PropertiesMessageServiceImpl(env);
    }
}
