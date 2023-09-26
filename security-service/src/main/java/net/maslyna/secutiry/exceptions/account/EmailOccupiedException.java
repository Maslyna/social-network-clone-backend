package net.maslyna.secutiry.exceptions.account;

import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;

public class EmailOccupiedException extends GlobalSecurityServiceException {
    public EmailOccupiedException() {
    }

    public EmailOccupiedException(String message) {
        super(message);
    }

    public EmailOccupiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
