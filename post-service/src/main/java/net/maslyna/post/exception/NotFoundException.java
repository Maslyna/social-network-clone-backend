package net.maslyna.post.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class NotFoundException extends GlobalPostServiceException {
    public NotFoundException(HttpStatusCode status) {
        super(status);
    }

    public NotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public NotFoundException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }

}
