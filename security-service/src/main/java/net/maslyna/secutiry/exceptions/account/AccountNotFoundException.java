package net.maslyna.secutiry.exceptions.account;

import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;
import org.springframework.http.HttpStatusCode;

public class AccountNotFoundException extends GlobalSecurityServiceException {
    public AccountNotFoundException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
