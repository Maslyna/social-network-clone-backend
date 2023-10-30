package net.maslyna.user.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class UserNotFoundException extends GlobalUserServiceException {

    public UserNotFoundException(HttpStatusCode status) {
        super(status);
    }

    public UserNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public UserNotFoundException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
