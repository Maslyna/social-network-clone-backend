package net.maslyna.post.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class FeignExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException e) {
        log.error("Handle Feign Exception: {}", e.contentUTF8());
        try {
            return ResponseEntity.status(e.status()).body(objectMapper.readValue(e.contentUTF8(), Object.class));
        } catch (IOException ignored) {
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }

}
