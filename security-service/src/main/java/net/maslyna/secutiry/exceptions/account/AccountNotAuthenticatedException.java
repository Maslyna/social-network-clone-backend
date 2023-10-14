package net.maslyna.secutiry.exceptions.account;

import net.maslyna.secutiry.exceptions.AuthenticationException;
import org.springframework.http.HttpStatusCode;

public class AccountNotAuthenticatedException extends AuthenticationException {
    public AccountNotAuthenticatedException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
