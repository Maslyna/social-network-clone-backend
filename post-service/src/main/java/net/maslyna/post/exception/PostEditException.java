package net.maslyna.post.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class PostEditException extends GlobalPostServiceException{
    public PostEditException(HttpStatusCode status) {
        super(status);
    }

    public PostEditException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public PostEditException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
