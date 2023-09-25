package net.maslyna.apigateway.exception;

import org.springframework.http.HttpStatusCode;

public class MissingAuthHeaderException extends GlobalApiGatewayException {
    public MissingAuthHeaderException(HttpStatusCode status) {
        super(status);
    }

    public MissingAuthHeaderException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public MissingAuthHeaderException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public MissingAuthHeaderException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public MissingAuthHeaderException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
