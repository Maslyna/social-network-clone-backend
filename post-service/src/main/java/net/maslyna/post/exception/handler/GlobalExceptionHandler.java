package net.maslyna.post.exception.handler;

import net.maslyna.common.message.MessageType;
import net.maslyna.post.exception.GlobalPostServiceException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static net.maslyna.common.message.MessageType.ERROR;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalPostServiceException.class)
    public ResponseEntity<?> handleGlobalPostServiceException(GlobalPostServiceException e) {
        return ResponseEntity.status(e.getStatusCode())
                .body(createDefaultResponse(
                        e.getStatusCode(),
                        ERROR,
                        e.getMessage()
                ));
    }

    private Map<String, Object> createDefaultResponse(HttpStatusCode status, MessageType type, String message) {
        return new HashMap<>(Map.of(
                "createdAt", Instant.now(),
                "messageType", type,
                "status", status.value(),
                "statusCode", status,
                "message", message
        ));
    }
}
