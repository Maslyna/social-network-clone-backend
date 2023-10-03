package net.maslyna.user.exception;

import org.springframework.http.HttpStatusCode;

public class UserAlreadyExistsException extends GlobalUserServiceException {

    public UserAlreadyExistsException(HttpStatusCode status) {
        super(status);
    }

    public UserAlreadyExistsException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public UserAlreadyExistsException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public UserAlreadyExistsException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected UserAlreadyExistsException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
