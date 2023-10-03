package net.maslyna.secutiry.exceptions.account;

import net.maslyna.secutiry.exceptions.AuthenticationException;
import org.springframework.http.HttpStatusCode;

public class AccountNotAuthenticatedException extends AuthenticationException {

    public AccountNotAuthenticatedException(HttpStatusCode status) {
        super(status);
    }

    public AccountNotAuthenticatedException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public AccountNotAuthenticatedException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public AccountNotAuthenticatedException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected AccountNotAuthenticatedException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
