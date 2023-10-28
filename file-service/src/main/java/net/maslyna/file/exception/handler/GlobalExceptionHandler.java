package net.maslyna.file.exception.handler;

import net.maslyna.common.message.MessageType;
import net.maslyna.common.response.ErrorMessageResponse;
import net.maslyna.file.exception.GlobalFileServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalFileServiceException.class)
    public ResponseEntity<?> handleGlobalFileServiceException(GlobalFileServiceException e) {
        return ResponseEntity.status(e.getStatusCode().value())
                .body(getDefaultResponseBody(e));
    }

    private ErrorMessageResponse getDefaultResponseBody(GlobalFileServiceException e) {
        return ErrorMessageResponse.builder()
                .statusCode(HttpStatus.valueOf(e.getStatusCode().value()))
                .message(e.getMessage())
                .type(MessageType.ERROR)
                .status(e.getStatusCode().value())
                .createdAt(Instant.now())
                .details(e.getDetails())
                .build();
    }
}
