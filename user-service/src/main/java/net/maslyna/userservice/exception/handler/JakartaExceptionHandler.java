package net.maslyna.userservice.exception.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import net.maslyna.userservice.model.dto.MessageType;
import net.maslyna.userservice.service.PropertiesMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Set;

@ControllerAdvice
@RequiredArgsConstructor
public class JakartaExceptionHandler {
    private final PropertiesMessageService messageService;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleViolationException(ConstraintViolationException e) {
        List<JakartaErrorMessage> errors = e.getConstraintViolations()
                .stream()
                .map(constraint -> {
                    String value = constraint.getInvalidValue().toString();
                    String message = messageService.getProperty(constraint.getMessage());
                    return new JakartaErrorMessage(value, message);
                })
                .toList();

        return getResponseEntity(HttpStatus.BAD_REQUEST, MessageType.VALIDATION_ERROR, errors);
    }

    private ResponseEntity<?> getResponseEntity(HttpStatus status, MessageType type, List<JakartaErrorMessage> errors) {
        return ResponseEntity.status(status).body(
                JakartaErrorResponse.builder()
                        .status(status)
                        .title(type)
                        .errors(errors)
                        .build()
        );
    }
}

@Builder
record JakartaErrorResponse(
        HttpStatus status,
        MessageType title,
        List<JakartaErrorMessage> errors
) {
}

@Builder
record JakartaErrorMessage(
        String value,
        String errorMessage
) {
}