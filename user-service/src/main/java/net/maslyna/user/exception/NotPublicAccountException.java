package net.maslyna.user.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class NotPublicAccountException extends GlobalUserServiceException{
    public NotPublicAccountException(HttpStatusCode status) {
        super(status);
    }

    public NotPublicAccountException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public NotPublicAccountException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
