package net.maslyna.secutiry.exceptions.account;

import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;

public class AuthenticationException extends GlobalSecurityServiceException {
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
