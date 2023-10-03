package net.maslyna.secutiry.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GlobalSecurityServiceException extends ResponseStatusException {

    public GlobalSecurityServiceException(HttpStatusCode status) {
        super(status);
    }

    public GlobalSecurityServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalSecurityServiceException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public GlobalSecurityServiceException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected GlobalSecurityServiceException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
