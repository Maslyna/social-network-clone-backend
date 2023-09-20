package net.maslyna.secutiryservice.exceptions.account;

import net.maslyna.secutiryservice.exceptions.GlobalSecurityServiceException;

public class AccountNotAuthenticatedException extends GlobalSecurityServiceException {
    public AccountNotAuthenticatedException() {
    }

    public AccountNotAuthenticatedException(String message) {
        super(message);
    }

    public AccountNotAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
