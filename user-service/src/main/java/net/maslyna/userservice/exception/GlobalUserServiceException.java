package net.maslyna.userservice.exception;

public class GlobalUserServiceException extends RuntimeException {
    public GlobalUserServiceException() {
    }

    public GlobalUserServiceException(String message, Object... data) {
        super(message.formatted(data));
    }

    public GlobalUserServiceException(String message) {
        super(message);
    }

    public GlobalUserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
