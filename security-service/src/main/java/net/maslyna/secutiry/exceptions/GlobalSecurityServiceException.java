package net.maslyna.secutiry.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GlobalSecurityServiceException extends ResponseStatusException {

    public GlobalSecurityServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

}
