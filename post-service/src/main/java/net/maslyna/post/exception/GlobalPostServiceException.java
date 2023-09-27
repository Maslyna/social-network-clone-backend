package net.maslyna.post.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GlobalPostServiceException extends ResponseStatusException {
    public GlobalPostServiceException(HttpStatusCode status) {
        super(status);
    }

    public GlobalPostServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalPostServiceException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public GlobalPostServiceException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected GlobalPostServiceException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
