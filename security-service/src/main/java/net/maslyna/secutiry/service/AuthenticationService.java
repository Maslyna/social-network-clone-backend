package net.maslyna.secutiry.service;

import net.maslyna.secutiry.model.dto.request.AuthenticationRequest;
import net.maslyna.secutiry.model.dto.request.RegistrationRequest;
import net.maslyna.secutiry.model.dto.response.AccountResponse;
import net.maslyna.secutiry.model.dto.response.AuthenticationResponse;
import net.maslyna.secutiry.model.entity.Account;
import org.springframework.transaction.annotation.Transactional;

public interface AuthenticationService {
    @Transactional
    AuthenticationResponse registration(RegistrationRequest request);

    @Transactional
    AuthenticationResponse authenticate(Account account);

    @Transactional(readOnly = true)
    AccountResponse validateToken(String jwt);

    AccountResponse getUserInfo(Account account);

    AuthenticationResponse edit(Account account, AuthenticationRequest request);
}
