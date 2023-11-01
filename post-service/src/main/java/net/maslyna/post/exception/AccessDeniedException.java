package net.maslyna.post.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class AccessDeniedException extends GlobalPostServiceException {
    public AccessDeniedException(HttpStatusCode status) {
        super(status);
    }

    public AccessDeniedException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public AccessDeniedException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
