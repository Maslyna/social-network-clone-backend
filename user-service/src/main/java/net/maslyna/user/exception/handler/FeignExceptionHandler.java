package net.maslyna.user.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class FeignExceptionHandler {
    private final ObjectMapper mapper;

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException e) {
        try {
            return ResponseEntity.status(e.status()).body(mapper.readValue(e.contentUTF8(), Object.class));
        } catch (IOException exception) {
            log.error(exception.getMessage());
            return ResponseEntity.status(e.status()).body(e.contentUTF8());
        }
    }
}
