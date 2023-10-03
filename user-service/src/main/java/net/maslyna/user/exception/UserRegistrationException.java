package net.maslyna.user.exception;

import org.springframework.http.HttpStatusCode;

public class UserRegistrationException extends GlobalUserServiceException{
    public UserRegistrationException(HttpStatusCode status) {
        super(status);
    }

    public UserRegistrationException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public UserRegistrationException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public UserRegistrationException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected UserRegistrationException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
