package net.maslyna.post.exception;

import org.springframework.http.HttpStatusCode;

public class CommentNotFoundException extends GlobalPostServiceException{
    public CommentNotFoundException(HttpStatusCode status) {
        super(status);
    }

    public CommentNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public CommentNotFoundException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public CommentNotFoundException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected CommentNotFoundException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
