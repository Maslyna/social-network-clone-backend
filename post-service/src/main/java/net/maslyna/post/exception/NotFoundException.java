package net.maslyna.post.exception;

import org.springframework.http.HttpStatusCode;

public class NotFoundException extends GlobalPostServiceException {
    public NotFoundException(HttpStatusCode status) {
        super(status);
    }

    public NotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public NotFoundException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public NotFoundException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public NotFoundException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
