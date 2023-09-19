package net.maslyna.secutiryservice.service;

import lombok.RequiredArgsConstructor;
import net.maslyna.secutiryservice.exceptions.account.TokenNotValidException;
import net.maslyna.secutiryservice.model.entity.Account;
import net.maslyna.secutiryservice.model.entity.Token;
import net.maslyna.secutiryservice.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final PropertiesMessageService message;

    public Token createToken(Account account) {
        return tokenRepository.save(
                Token.builder()
                        .jwt(jwtService.generateToken(account))
                        .account(account)
                        .build()
        );
    }

    public boolean deletePreviousToken(Account account) {
        return tokenRepository.deleteByAccount(account) > 0;
    }

    public Account validate(String jwt) {
        Token token = tokenRepository.findByJwt(jwt)
                .orElseThrow(() -> new TokenNotValidException(
                        message.getProperty("error.account.token.not-found")
                ));
        if (!jwtService.isTokenValid(jwt, token.getAccount())) {
            throw new TokenNotValidException(message.getProperty("error.account.token.not-valid"));
        }
        return token.getAccount();
    }
}