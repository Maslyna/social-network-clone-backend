package net.maslyna.secutiryservice.exceptions.account;

import net.maslyna.secutiryservice.exceptions.GlobalSecurityServiceException;

public class AccountNotFoundException extends GlobalSecurityServiceException {
    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
