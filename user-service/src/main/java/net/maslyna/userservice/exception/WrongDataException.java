package net.maslyna.userservice.exception;

public class WrongDataException extends GlobalUserServiceException {
    public WrongDataException() {
    }

    public WrongDataException(String message, Object... data) {
        super(message, data);
    }

    public WrongDataException(String message) {
        super(message);
    }

    public WrongDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
