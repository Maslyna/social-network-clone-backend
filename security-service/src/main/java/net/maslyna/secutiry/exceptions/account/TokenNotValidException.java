package net.maslyna.secutiry.exceptions.account;

public class TokenNotValidException extends AuthenticationException {
    public TokenNotValidException() {
    }

    public TokenNotValidException(String message) {
        super(message);
    }

    public TokenNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
