package net.maslyna.common.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

public class AbstractServiceException extends ResponseStatusException {
    private final Map<String, Object> details = new HashMap<>();

    public AbstractServiceException(HttpStatusCode status) {
        super(status);
    }

    public AbstractServiceException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public AbstractServiceException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason);
        this.details.putAll(details);
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
