package net.maslyna.apigateway.exception;

public class ValidationRequestException extends GlobalApiGatewayException {
    public ValidationRequestException() {
    }

    public ValidationRequestException(String message) {
        super(message);
    }

    public ValidationRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
