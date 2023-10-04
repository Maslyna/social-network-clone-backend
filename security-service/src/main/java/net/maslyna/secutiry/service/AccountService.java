package net.maslyna.secutiry.service;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.secutiry.exceptions.account.AccountNotFoundException;
import net.maslyna.secutiry.model.Role;
import net.maslyna.secutiry.model.entity.Account;
import net.maslyna.secutiry.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PropertiesMessageService messageService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(
                        HttpStatus.NOT_FOUND,
                        messageService.getProperty("error.account.not-found")
                ));
    }

    @Transactional
    public Account createUserAccount(Long id, String email, String password) {
        return accountRepository.save(
                Account.builder()
                        .id(id)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .role(Role.USER)
                        .isEnabled(true)
                        .isAccountNonLocked(true)
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public boolean isUserAlreadyExists(Long id, String email) {
        return accountRepository.existsByEmailIgnoreCase(email)
                || accountRepository.existsById(id);
    }
}
