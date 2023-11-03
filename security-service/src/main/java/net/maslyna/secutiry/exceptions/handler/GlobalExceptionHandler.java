package net.maslyna.secutiry.exceptions.handler;

import net.maslyna.common.message.MessageType;
import net.maslyna.common.response.ErrorMessageResponse;
import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.security.GeneralSecurityException;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeneralSecurityException.class)
    public ResponseEntity<?> handleResponseStatusException(
            GlobalSecurityServiceException e
    ) {
        return createDefaultErrorResponse(e);
    }

    private ResponseEntity<?> createDefaultErrorResponse(ResponseStatusException e) {
        return ResponseEntity.status(HttpStatus.valueOf(e.getStatusCode().value()))
                .body(getErrorMessageBody(e));
    }

    private ErrorMessageResponse getErrorMessageBody(ResponseStatusException e) {
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        return ErrorMessageResponse.builder()
                .message(e.getReason())
                .createdAt(Instant.now())
                .statusCode(status)
                .status(status.value())
                .type(MessageType.ERROR)
                .build();
    }

}
