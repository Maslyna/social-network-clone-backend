package net.maslyna.secutiryservice.exceptions.account;

import net.maslyna.secutiryservice.exceptions.GlobalSecurityServiceException;

public class TokenNotValidException extends AuthenticationException {
    public TokenNotValidException() {
    }

    public TokenNotValidException(String message) {
        super(message);
    }

    public TokenNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
