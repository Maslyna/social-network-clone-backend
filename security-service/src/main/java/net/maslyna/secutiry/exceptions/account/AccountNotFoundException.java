package net.maslyna.secutiry.exceptions.account;

import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;

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
