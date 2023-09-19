package net.maslyna.apigateway.exception;

public class MissingAuthHeaderException extends GlobalApiGatewayException {
    public MissingAuthHeaderException() {
    }

    public MissingAuthHeaderException(String message) {
        super(message);
    }

    public MissingAuthHeaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
