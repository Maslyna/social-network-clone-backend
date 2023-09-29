package net.maslyna.post.exception.handler;

import net.maslyna.post.exception.GlobalPostServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static net.maslyna.common.message.MessageType.VALIDATION_ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalPostServiceException.class)
    public ResponseEntity<?> handleGlobalPostServiceException(GlobalPostServiceException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(
                        createDefaultResponse(
                                HttpStatus.valueOf(e.getStatusCode().value()),
                                e.getMessage())
                );
    }

    private Map<String, Object> createDefaultResponse(HttpStatus status, String message) {
        return new HashMap<>(Map.of(
                "createdAt", Instant.now(),
                "messageType", VALIDATION_ERROR,
                "status", status.value(),
                "statusCode", status,
                "message", message
        ));
    }
}
