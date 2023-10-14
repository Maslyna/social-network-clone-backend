package net.maslyna.secutiry.exceptions.account;

import net.maslyna.secutiry.exceptions.AuthenticationException;
import org.springframework.http.HttpStatusCode;

public class TokenNotValidException extends AuthenticationException {
    public TokenNotValidException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
