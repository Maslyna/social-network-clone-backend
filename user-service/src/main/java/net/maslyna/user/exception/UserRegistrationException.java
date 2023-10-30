package net.maslyna.user.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class UserRegistrationException extends GlobalUserServiceException{
    public UserRegistrationException(HttpStatusCode status) {
        super(status);
    }

    public UserRegistrationException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public UserRegistrationException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
