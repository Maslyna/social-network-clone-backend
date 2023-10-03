package net.maslyna.user.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GlobalUserServiceException extends ResponseStatusException {

    public GlobalUserServiceException(HttpStatusCode status) {
        super(status);
    }

    public GlobalUserServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalUserServiceException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public GlobalUserServiceException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected GlobalUserServiceException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
