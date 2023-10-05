package net.maslyna.follower.exception;

import org.springframework.http.HttpStatusCode;

public class AccessDeniedException extends GlobalFollowerServiceException {
    public AccessDeniedException(HttpStatusCode status) {
        super(status);
    }

    public AccessDeniedException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public AccessDeniedException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public AccessDeniedException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public AccessDeniedException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
