package net.maslyna.secutiry.exceptions;

public class GlobalSecurityServiceException extends RuntimeException {
    public GlobalSecurityServiceException() {
        super();
    }

    public GlobalSecurityServiceException(String message) {
        super(message);
    }

    public GlobalSecurityServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
