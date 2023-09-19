package net.maslyna.secutiryservice.config;

import net.maslyna.secutiryservice.exceptions.account.AccountNotFoundException;
import net.maslyna.secutiryservice.repository.AccountRepository;
import net.maslyna.secutiryservice.service.PropertiesMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
}
