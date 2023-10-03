package net.maslyna.user.exception;

import org.springframework.http.HttpStatusCode;

public class WrongDataException extends GlobalUserServiceException {

    public WrongDataException(HttpStatusCode status) {
        super(status);
    }

    public WrongDataException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public WrongDataException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public WrongDataException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected WrongDataException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
