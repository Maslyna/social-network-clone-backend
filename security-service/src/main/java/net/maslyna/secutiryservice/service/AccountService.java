package net.maslyna.secutiryservice.service;

import lombok.RequiredArgsConstructor;
import net.maslyna.secutiryservice.exceptions.account.AccountNotFoundException;
import net.maslyna.secutiryservice.model.Role;
import net.maslyna.secutiryservice.model.dto.request.AuthenticationRequest;
import net.maslyna.secutiryservice.model.entity.Account;
import net.maslyna.secutiryservice.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PropertiesMessageService messageService;
    private final PasswordEncoder passwordEncoder;

    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(
                        messageService.getProperty("error.account.not-found")
                ));
    }

    public Account createUserAccount(String email, String password) {
        return accountRepository.save(
                Account.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .role(Role.USER)
                        .isEnabled(true)
                        .isAccountNonLocked(true)
                        .build()
        );
    }

    public boolean isUserAlreadyExists(String email) {
        return accountRepository.existsByEmailIgnoreCase(email);
    }
}
