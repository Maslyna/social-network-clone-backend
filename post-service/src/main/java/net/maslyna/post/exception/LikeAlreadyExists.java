package net.maslyna.post.exception;

import org.springframework.http.HttpStatusCode;

public class LikeAlreadyExists extends GlobalPostServiceException {
    public LikeAlreadyExists(HttpStatusCode status) {
        super(status);
    }

    public LikeAlreadyExists(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public LikeAlreadyExists(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public LikeAlreadyExists(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected LikeAlreadyExists(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
