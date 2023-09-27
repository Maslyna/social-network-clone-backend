package net.maslyna.post.exception;

import org.springframework.http.HttpStatusCode;

public class PostNotFoundException extends GlobalPostServiceException {
    public PostNotFoundException(HttpStatusCode status) {
        super(status);
    }

    public PostNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public PostNotFoundException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public PostNotFoundException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected PostNotFoundException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
