package net.maslyna.apigateway.exception;

import org.springframework.http.HttpStatusCode;

public class ValidationRequestException extends GlobalApiGatewayException {
    public ValidationRequestException(HttpStatusCode status) {
        super(status);
    }

    public ValidationRequestException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public ValidationRequestException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public ValidationRequestException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public ValidationRequestException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
