package net.maslyna.user.exception.handler;

import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import net.maslyna.user.model.dto.MessageType;
import net.maslyna.user.service.PropertiesMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

import static net.maslyna.user.model.dto.MessageType.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@RequiredArgsConstructor
public class JakartaExceptionHandler {
    private final PropertiesMessageService messageService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<JakartaErrorMessage> errors = e.getAllErrors()
                .stream()
                .map(error -> {
                    // TODO: make more readable
                    Object value = error.getCodes() != null && error.getCodes().length > 1 ? error.getCodes()[1] : null;
                    String message = error.getDefaultMessage();
                    return new JakartaErrorMessage(value, message);
                }).toList();
        return getResponseEntity(BAD_REQUEST, VALIDATION_ERROR, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleViolationException(ConstraintViolationException e) {
        List<JakartaErrorMessage> errors = e.getConstraintViolations()
                .stream()
                .map(constraint -> {
                    Object value = constraint.getInvalidValue();
                    String message = constraint.getMessage();
                    return new JakartaErrorMessage(value, message);
                })
                .toList();

        return getResponseEntity(BAD_REQUEST, VALIDATION_ERROR, errors);
    }

    private ResponseEntity<?> getResponseEntity(HttpStatus status, MessageType type, List<JakartaErrorMessage> errors) {
        return ResponseEntity.status(status).body(
                JakartaErrorResponse.builder()
                        .createdAt(Instant.now())
                        .status(status)
                        .statusCode(status.value())
                        .title(type)
                        .errors(errors)
                        .build()
        );
    }
}

//TODO: refactor
@Builder
record JakartaErrorResponse(
        Instant createdAt,
        HttpStatus status,
        int statusCode,
        MessageType title,
        List<JakartaErrorMessage> errors
) {
}

@Builder
record JakartaErrorMessage(
        Object value,
        String errorMessage
) {
}