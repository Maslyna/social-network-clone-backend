package net.maslyna.follower.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GlobalFollowerServiceException extends ResponseStatusException {
    public GlobalFollowerServiceException(HttpStatusCode status) {
        super(status);
    }

    public GlobalFollowerServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalFollowerServiceException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public GlobalFollowerServiceException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected GlobalFollowerServiceException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
