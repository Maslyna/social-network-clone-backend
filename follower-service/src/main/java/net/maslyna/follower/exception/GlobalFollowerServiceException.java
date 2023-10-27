package net.maslyna.follower.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GlobalFollowerServiceException extends ResponseStatusException {
    public GlobalFollowerServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalFollowerServiceException(HttpStatusCode status) {
        super(status);
    }
}
