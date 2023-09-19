package net.maslyna.secutiryservice.exceptions.account;

import net.maslyna.secutiryservice.exceptions.GlobalSecurityServiceException;

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
