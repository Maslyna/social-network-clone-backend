package net.maslyna.user.exception.handler;

import lombok.Builder;
import net.maslyna.user.exception.*;
import net.maslyna.user.model.dto.MessageType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

import static net.maslyna.user.model.dto.MessageType.ERROR;
import static net.maslyna.user.model.dto.MessageType.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalUserServiceException.class)
    public ResponseEntity<?> handleGlobalUserServiceException(GlobalUserServiceException e) {
        return getResponseEntity(NOT_IMPLEMENTED, ERROR, e);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return getResponseEntity(CONFLICT, ERROR, e);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        return getResponseEntity(NOT_FOUND, ERROR, e);
    }

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<?> handleWrongDataException(WrongDataException e) {
        return getResponseEntity(BAD_REQUEST, VALIDATION_ERROR, e);
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<?> handleUserRegistrationException(UserRegistrationException e) {
        return getResponseEntity(SERVICE_UNAVAILABLE, ERROR, e);
    }

    private ResponseEntity<?> getResponseEntity(HttpStatus status, MessageType messageType, Throwable throwable) {
        return ResponseEntity.status(status).body(
                MessageErrorResponse.builder()
                        .createdAt(Instant.now())
                        .title(messageType)
                        .status(status.value())
                        .statusCode(status)
                        .message(throwable.getMessage())
                        .build()
        );
    }
}

// TODO: refactor
@Builder
record MessageErrorResponse(
        Instant createdAt,
        MessageType title,
        int status,
        HttpStatus statusCode,
        String message

) {

}
