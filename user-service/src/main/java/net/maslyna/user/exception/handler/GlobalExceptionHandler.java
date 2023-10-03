package net.maslyna.user.exception.handler;

import net.maslyna.common.message.MessageType;
import net.maslyna.user.exception.GlobalUserServiceException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalUserServiceException.class)
    public ResponseEntity<Map<String, Object>> handleGlobalUserServiceException(GlobalUserServiceException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(createErrorResponseWithMessage(e.getStatusCode(), e.getMessage()));
    }

    private Map<String, Object> createErrorResponseWithMessage(
            HttpStatusCode status,
            String message) {
        return Map.of(
                "createdAt", Instant.now(),
                "title", MessageType.ERROR,
                "status", status.value(),
                "statusCode", status,
                "message", message
        );
    }
}
