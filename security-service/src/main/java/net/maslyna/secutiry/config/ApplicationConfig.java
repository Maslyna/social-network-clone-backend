package net.maslyna.secutiry.config;

import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.common.service.impl.PropertiesMessageServiceImpl;
import net.maslyna.secutiry.exceptions.account.AccountNotFoundException;
import net.maslyna.secutiry.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {
    @Bean
    public UserDetailsService userDetailsService(
            AccountRepository accountRepository,
            PropertiesMessageService propertiesMessageService) {
        return email -> accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(
                        HttpStatus.NOT_FOUND,
                        propertiesMessageService.getProperty("error.account.not-found")
                ));
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PropertiesMessageService messageService(Environment env) {
        return new PropertiesMessageServiceImpl(env);
    }
}
