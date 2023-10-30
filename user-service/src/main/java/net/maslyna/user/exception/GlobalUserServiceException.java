package net.maslyna.user.exception;

import net.maslyna.common.exception.AbstractServiceException;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class GlobalUserServiceException extends AbstractServiceException {

    public GlobalUserServiceException(HttpStatusCode status) {
        super(status);
    }

    public GlobalUserServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalUserServiceException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
