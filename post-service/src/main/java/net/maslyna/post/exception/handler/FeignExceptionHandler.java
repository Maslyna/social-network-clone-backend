package net.maslyna.post.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class FeignExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException e) {
        try {
            return ResponseEntity.status(e.status()).body(objectMapper.readValue(e.contentUTF8(), Map.class));
        } catch (IOException ignored) {
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }

}
