package net.maslyna.secutiryservice.exceptions.account;

import net.maslyna.secutiryservice.exceptions.GlobalSecurityServiceException;

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
