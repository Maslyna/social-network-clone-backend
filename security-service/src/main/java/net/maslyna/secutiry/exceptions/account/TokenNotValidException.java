package net.maslyna.secutiry.exceptions.account;

import net.maslyna.secutiry.exceptions.AuthenticationException;
import org.springframework.http.HttpStatusCode;

public class TokenNotValidException extends AuthenticationException {

    public TokenNotValidException(HttpStatusCode status) {
        super(status);
    }

    public TokenNotValidException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public TokenNotValidException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public TokenNotValidException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected TokenNotValidException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
