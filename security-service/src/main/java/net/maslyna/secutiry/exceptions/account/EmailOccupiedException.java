package net.maslyna.secutiry.exceptions.account;

import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;
import org.springframework.http.HttpStatusCode;

public class EmailOccupiedException extends GlobalSecurityServiceException {

    public EmailOccupiedException(HttpStatusCode status) {
        super(status);
    }

    public EmailOccupiedException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public EmailOccupiedException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public EmailOccupiedException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected EmailOccupiedException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
