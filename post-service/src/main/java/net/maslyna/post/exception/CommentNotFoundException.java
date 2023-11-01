package net.maslyna.post.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class CommentNotFoundException extends GlobalPostServiceException{
    public CommentNotFoundException(HttpStatusCode status) {
        super(status);
    }

    public CommentNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public CommentNotFoundException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
