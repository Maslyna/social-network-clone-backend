package net.maslyna.post.exception;

import net.maslyna.common.exception.AbstractServiceException;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class GlobalPostServiceException extends AbstractServiceException {
    public GlobalPostServiceException(HttpStatusCode status) {
        super(status);
    }

    public GlobalPostServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalPostServiceException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
