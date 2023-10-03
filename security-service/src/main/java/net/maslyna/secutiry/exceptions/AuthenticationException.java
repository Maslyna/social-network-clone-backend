package net.maslyna.secutiry.exceptions;

import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;
import org.springframework.http.HttpStatusCode;

public class AuthenticationException extends GlobalSecurityServiceException {

    public AuthenticationException(HttpStatusCode status) {
        super(status);
    }

    public AuthenticationException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public AuthenticationException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public AuthenticationException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected AuthenticationException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
