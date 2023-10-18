package net.maslyna.user.exception.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import net.maslyna.common.service.PropertiesMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.maslyna.common.message.MessageType.VALIDATION_ERROR;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@RequiredArgsConstructor
public class ValidationHandler {
    private final PropertiesMessageService messageService;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        HttpStatus status = BAD_REQUEST;
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        Map<String, Object> response = new HashMap<>(createDefaultResponse(status));
        List<?> additionalMessages = violations.stream()
                .map(exception -> ErrorMessage.builder()
                        .message(getMessage(exception.getMessage()))
                        .invalidValue(exception.getInvalidValue())
                        .build()
                ).toList();
        response.put("errors", additionalMessages);

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HttpStatus status = BAD_REQUEST;
        Map<String, Object> response = new HashMap<>(createDefaultResponse(status));

        response.put("errors", e.getAllErrors());

        return ResponseEntity.status(status).body(response);
    }

    private Map<String, Object> createDefaultResponse(HttpStatusCode status) {
        return Map.of(
                "createdAt", Instant.now(),
                "messageType", VALIDATION_ERROR,
                "status", status.value(),
                "statusCode", status
        );
    }

    private String getMessage(String message) {
        String result = messageService.getProperty(message);
        return result != null ? result : message;
    }

    @Builder
    private record ErrorMessage(
            String message,
            Object invalidValue

    ) {
    }
}

