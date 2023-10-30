package net.maslyna.user.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class UserAlreadyExistsException extends GlobalUserServiceException {

    public UserAlreadyExistsException(HttpStatusCode status) {
        super(status);
    }

    public UserAlreadyExistsException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public UserAlreadyExistsException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
