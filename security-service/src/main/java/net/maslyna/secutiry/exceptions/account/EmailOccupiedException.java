package net.maslyna.secutiry.exceptions.account;

import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;
import org.springframework.http.HttpStatusCode;

public class EmailOccupiedException extends GlobalSecurityServiceException {
    public EmailOccupiedException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

}
