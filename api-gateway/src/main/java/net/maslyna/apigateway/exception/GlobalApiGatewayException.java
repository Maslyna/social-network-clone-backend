package net.maslyna.apigateway.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GlobalApiGatewayException extends ResponseStatusException {
    public GlobalApiGatewayException(HttpStatusCode status) {
        super(status);
    }

    public GlobalApiGatewayException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalApiGatewayException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

    public GlobalApiGatewayException(HttpStatusCode status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    protected GlobalApiGatewayException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
