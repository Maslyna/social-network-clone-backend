package net.maslyna.userservice.exceptions;

public class UserRegistrationException extends GlobalUserServiceException{
    public UserRegistrationException() {
    }

    public UserRegistrationException(String message) {
        super(message);
    }

    public UserRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
