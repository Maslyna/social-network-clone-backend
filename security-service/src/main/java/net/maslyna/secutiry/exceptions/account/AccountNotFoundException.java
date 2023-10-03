package net.maslyna.secutiry.exceptions.account;

import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;
import org.springframework.http.HttpStatusCode;

public class AccountNotFoundException extends GlobalSecurityServiceException {

    public AccountNotFoundException(HttpStatusCode status) {
        super(status);
    }

    public AccountNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public AccountNotFoundException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public AccountNotFoundException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected AccountNotFoundException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
