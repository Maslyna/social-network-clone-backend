package net.maslyna.secutiryservice.exceptions;

public class EmailOccupiedException extends RuntimeException {
    public EmailOccupiedException() {
    }

    public EmailOccupiedException(String message) {
        super(message);
    }

    public EmailOccupiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
