package net.maslyna.userservice.exceptions;

public class UserNotFoundException extends GlobalUserServiceException {
    public UserNotFoundException() {
    }

    public UserNotFoundException(String message, Object... data) {
        super(message, data);
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
