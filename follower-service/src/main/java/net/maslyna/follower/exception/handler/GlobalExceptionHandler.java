package net.maslyna.follower.exception.handler;

import lombok.RequiredArgsConstructor;
import net.maslyna.common.message.MessageType;
import net.maslyna.common.response.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(
            ResponseStatusException e
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
