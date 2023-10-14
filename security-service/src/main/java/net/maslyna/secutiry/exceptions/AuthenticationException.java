package net.maslyna.secutiry.exceptions;

import org.springframework.http.HttpStatusCode;

public class AuthenticationException extends GlobalSecurityServiceException {
    public AuthenticationException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
