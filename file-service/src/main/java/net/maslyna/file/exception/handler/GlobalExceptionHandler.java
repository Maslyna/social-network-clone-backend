package net.maslyna.file.exception.handler;

import net.maslyna.common.message.MessageType;
import net.maslyna.common.response.ErrorMessageResponse;
import net.maslyna.file.exception.GlobalFileServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.time.Instant;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalFileServiceException.class)
    public ResponseEntity<?> handleGlobalFileServiceException(GlobalFileServiceException e) {
        return ResponseEntity.status(e.getStatusCode().value())
                .body(getDefaultResponseBody(e));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartException(MultipartException e) {
        return ResponseEntity.status(BAD_REQUEST)
                .body(getDefaultResponseBody(BAD_REQUEST, e));
    }

    private ErrorMessageResponse getDefaultResponseBody(HttpStatus status, Exception e) {
        return ErrorMessageResponse.builder()
                .statusCode(status)
                .status(status.value())
                .type(MessageType.ERROR)
                .createdAt(Instant.now())
                .message(e.getMessage())
                .details(Map.of())
                .build();
    }

    private ErrorMessageResponse getDefaultResponseBody(GlobalFileServiceException e) {
        return ErrorMessageResponse.builder()
                .statusCode(HttpStatus.valueOf(e.getStatusCode().value()))
                .message(e.getReason())
                .type(MessageType.ERROR)
                .status(e.getStatusCode().value())
                .createdAt(Instant.now())
                .details(e.getDetails())
                .build();
    }
}
