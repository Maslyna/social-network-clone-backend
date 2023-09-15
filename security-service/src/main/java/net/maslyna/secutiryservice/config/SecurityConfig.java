package net.maslyna.secutiryservice.config;

import lombok.RequiredArgsConstructor;
import net.maslyna.secutiryservice.exceptions.AccountNotFoundException;
import net.maslyna.secutiryservice.repository.AccountRepository;
import net.maslyna.secutiryservice.service.DefaultMessageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // TODO: IMPLEMENT SECURITY CONFIG
    private final DefaultMessageService defaultMessageService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/api/v1/security/register").permitAll();
                    request.anyRequest().authenticated();}); // TODO: continue
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(
            AccountRepository accountRepository
    ) {
        return email -> accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(
                        defaultMessageService.getProperty("error.account.account-not-found") + " " + email
                ));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
