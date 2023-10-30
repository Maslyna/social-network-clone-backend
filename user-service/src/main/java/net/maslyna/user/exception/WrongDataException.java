package net.maslyna.user.exception;

import org.springframework.http.HttpStatusCode;

import java.util.Map;

public class WrongDataException extends GlobalUserServiceException {

    public WrongDataException(HttpStatusCode status) {
        super(status);
    }

    public WrongDataException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public WrongDataException(HttpStatusCode status, String reason, Map<String, Object> details) {
        super(status, reason, details);
    }
}
