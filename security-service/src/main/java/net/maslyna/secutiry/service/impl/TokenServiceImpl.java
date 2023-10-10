package net.maslyna.secutiry.service.impl;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.secutiry.exceptions.account.TokenNotValidException;
import net.maslyna.secutiry.model.entity.Account;
import net.maslyna.secutiry.model.entity.Token;
import net.maslyna.secutiry.repository.TokenRepository;
import net.maslyna.secutiry.service.JwtService;
import net.maslyna.secutiry.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final PropertiesMessageService message;

    @Override@Transactional
    public Token createToken(Account account) {
        Token previousToken = getPreviousToken(account);
        if (previousToken != null) {
            return recreateToken(previousToken);
        }
        return tokenRepository.save(
                Token.builder()
                        .jwt(jwtService.generateToken(account, Map.of("userId", account.getId())))
                        .account(account)
                        .build()
        );
    }

    @Override@Transactional
    public boolean deletePreviousToken(Account account) {
        return tokenRepository.deleteByAccount(account) > 0;
    }

    @Override@Transactional(readOnly = true)
    public Account getAccount(String jwt) {
        Token token = tokenRepository.findByJwt(jwt)
                .orElseThrow(() -> new TokenNotValidException(
                        HttpStatus.NOT_FOUND,
                        message.getProperty("error.account.token.not-found")
                ));
        if (!jwtService.isTokenValid(jwt, token.getAccount())) {
            throw new TokenNotValidException(
                    HttpStatus.FORBIDDEN,
                    message.getProperty("error.account.token.not-valid")
            );
        }
        return token.getAccount();
    }

    private Token recreateToken(Token token) {
        token.setJwt(jwtService.generateToken(token.getAccount()));
        return token;
    }

    private Token getPreviousToken(Account account) {
        return tokenRepository.findByAccount_Id(account.getId());
    }
}
