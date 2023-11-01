package net.maslyna.post.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class LikeAlreadyExists extends GlobalPostServiceException {
    public LikeAlreadyExists(HttpStatusCode status) {
        super(status);
    }

    public LikeAlreadyExists(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public LikeAlreadyExists(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
