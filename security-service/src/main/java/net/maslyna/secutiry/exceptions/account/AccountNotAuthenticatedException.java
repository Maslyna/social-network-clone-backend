package net.maslyna.secutiry.exceptions.account;

public class AccountNotAuthenticatedException extends AuthenticationException {
    public AccountNotAuthenticatedException() {
    }

    public AccountNotAuthenticatedException(String message) {
        super(message);
    }

    public AccountNotAuthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
