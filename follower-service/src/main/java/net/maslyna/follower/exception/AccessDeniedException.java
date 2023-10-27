package net.maslyna.follower.exception;

import org.springframework.http.HttpStatusCode;

public class AccessDeniedException extends GlobalFollowerServiceException {
    public AccessDeniedException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public AccessDeniedException(HttpStatusCode status) {
        super(status);
    }
}
