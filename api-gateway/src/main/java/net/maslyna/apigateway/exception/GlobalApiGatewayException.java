package net.maslyna.apigateway.exception;

public class GlobalApiGatewayException extends RuntimeException {
    public GlobalApiGatewayException() {
    }

    public GlobalApiGatewayException(String message) {
        super(message);
    }

    public GlobalApiGatewayException(String message, Throwable cause) {
        super(message, cause);
    }
}
