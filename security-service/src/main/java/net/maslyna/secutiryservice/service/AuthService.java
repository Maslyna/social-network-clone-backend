package net.maslyna.secutiryservice.service;

import lombok.RequiredArgsConstructor;
import net.maslyna.secutiryservice.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;

    public Boolean registration(String email, String password) {

    }

    private boolean isEmailValid() {
        return true;
    }
}
