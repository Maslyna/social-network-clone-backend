package net.maslyna.post.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class PostNotFoundException extends GlobalPostServiceException {
    public PostNotFoundException(HttpStatusCode status) {
        super(status);
    }

    public PostNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public PostNotFoundException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
