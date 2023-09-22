package net.maslyna.secutiryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.secutiryservice.exceptions.account.AccountNotAuthenticatedException;
import net.maslyna.secutiryservice.exceptions.account.EmailOccupiedException;
import net.maslyna.secutiryservice.mapper.AccountMapper;
import net.maslyna.secutiryservice.model.dto.request.AuthenticationRequest;
import net.maslyna.secutiryservice.model.dto.response.AccountResponse;
import net.maslyna.secutiryservice.model.dto.response.AuthenticationResponse;
import net.maslyna.secutiryservice.model.entity.Account;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final PropertiesMessageService messageService;
    private final TokenService tokenService;
    private final AccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final AccountMapper accountMapper;

    @Transactional
    public AuthenticationResponse registration(AuthenticationRequest request) {
        //TODO: registration logic
        if (accountService.isUserAlreadyExists(request.email())) {
            throw new EmailOccupiedException(
                    messageService.getProperty("error.user.email.occupied")
            );
        }
        Account newAccount = accountService.createUserAccount(request.email(), request.password());

        log.info("new account {} created with id = {}", newAccount.getUsername(), newAccount.getId());
        return new AuthenticationResponse(
                tokenService.createToken(newAccount).getJwt()
        );
    }

    @Transactional
    public AuthenticationResponse authenticate(Account account) {
        if (account == null) {
            throw new AccountNotAuthenticatedException(
                    messageService.getProperty("error.account.not-authenticated")
            );
        }
        log.info("token generation for account {}", account.getEmail());
        return new AuthenticationResponse(
                tokenService.createToken(account).getJwt()
        );
    }

    @Transactional(readOnly = true)
    public AccountResponse validateToken(String jwt) {
        return accountMapper.accountToAccountResponse(tokenService.getAccount(jwt));
    }

    public AccountResponse getUserInfo(Account account) {
        if (account == null) {
            throw new AccountNotAuthenticatedException(
                    messageService.getProperty("error.account.not-authenticated")
            );
        }
        return accountMapper.accountToAccountResponse(account);
    }

}
