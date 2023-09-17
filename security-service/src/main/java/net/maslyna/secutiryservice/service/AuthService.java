package net.maslyna.secutiryservice.service;

import lombok.RequiredArgsConstructor;
import net.maslyna.secutiryservice.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public Boolean registration(String email, String password) {
        //TODO: registration logic
        return null;
    }

    private boolean isEmailValid() {
        return true;
    }
}
