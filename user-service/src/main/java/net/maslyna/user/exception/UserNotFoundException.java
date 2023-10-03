package net.maslyna.user.exception;

import org.springframework.http.HttpStatusCode;

public class UserNotFoundException extends GlobalUserServiceException {

    public UserNotFoundException(HttpStatusCode status) {
        super(status);
    }

    public UserNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public UserNotFoundException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public UserNotFoundException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected UserNotFoundException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
