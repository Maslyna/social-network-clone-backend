package net.maslyna.user.exception.handler;

import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FeignExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException e) {
        return getDefaultResponse(e);
    }

    private ResponseEntity<?> getDefaultResponse(FeignException e) {
        return ResponseEntity.status(e.status())
                .body(e.responseBody());
    }
}
