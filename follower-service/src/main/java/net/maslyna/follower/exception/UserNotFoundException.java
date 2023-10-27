package net.maslyna.follower.exception;

import org.springframework.http.HttpStatusCode;

public class UserNotFoundException extends GlobalFollowerServiceException{
    public UserNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

}
