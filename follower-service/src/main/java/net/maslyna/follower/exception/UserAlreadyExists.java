package net.maslyna.follower.exception;

import org.springframework.http.HttpStatusCode;

public class UserAlreadyExists extends GlobalFollowerServiceException {
    public UserAlreadyExists(HttpStatusCode status, String reason) {
        super(status, reason);
    }

}
