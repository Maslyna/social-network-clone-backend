package net.maslyna.follower.exception;

import org.springframework.http.HttpStatusCode;

public class UserAlreadyExists extends GlobalFollowerServiceException {
    public UserAlreadyExists(HttpStatusCode status) {
        super(status);
    }

    public UserAlreadyExists(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public UserAlreadyExists(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public UserAlreadyExists(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected UserAlreadyExists(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
