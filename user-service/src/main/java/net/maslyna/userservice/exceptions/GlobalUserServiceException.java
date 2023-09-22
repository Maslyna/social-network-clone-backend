package net.maslyna.userservice.exceptions;

public class GlobalUserServiceException extends RuntimeException {
    public GlobalUserServiceException() {
    }

    public GlobalUserServiceException(String message) {
        super(message);
    }

    public GlobalUserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}