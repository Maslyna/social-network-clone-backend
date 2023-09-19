package net.maslyna.secutiryservice.service;

import lombok.RequiredArgsConstructor;
import net.maslyna.secutiryservice.exceptions.account.AccountNotFoundException;
import net.maslyna.secutiryservice.exceptions.account.EmailOccupiedException;
import net.maslyna.secutiryservice.model.Role;
import net.maslyna.secutiryservice.model.dto.request.AuthenticationRequest;
import net.maslyna.secutiryservice.model.dto.response.AuthenticationResponse;
import net.maslyna.secutiryservice.model.entity.Account;
import net.maslyna.secutiryservice.repository.AccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final PropertiesMessageService messageService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registration(AuthenticationRequest request) {
        //TODO: registration logic
        if (accountRepository.existsByEmailIgnoreCase(request.email())) {
            throw new EmailOccupiedException(messageService.getProperty("error.account.email.occupied"));
        }
        Account newAccount = Account.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .isEnabled(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .build();
        accountRepository.save(newAccount);

        return new AuthenticationResponse(
                jwtService.generateToken(newAccount)
        );
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        //TODO: authentication logic
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        Account account = getAccountByEmail(request.email());
        return new AuthenticationResponse(
                jwtService.generateToken(account)
        );
    }

    private Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException(
                        messageService.getProperty("error.account.not-found")
                ));
    }
}
