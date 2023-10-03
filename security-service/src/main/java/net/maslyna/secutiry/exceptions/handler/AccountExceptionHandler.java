package net.maslyna.secutiry.exceptions.handler;

import net.maslyna.common.message.MessageType;
import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;
import net.maslyna.secutiry.exceptions.account.AccountNotFoundException;
import net.maslyna.secutiry.exceptions.AuthenticationException;
import net.maslyna.secutiry.exceptions.account.EmailOccupiedException;
import net.maslyna.secutiry.exceptions.account.TokenNotValidException;
import net.maslyna.secutiry.model.dto.response.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler(GlobalSecurityServiceException.class)
    public ResponseEntity<?> handleSecurityServiceException(
            GlobalSecurityServiceException e
    ) {
        e.getBody().setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(e.getStatusCode().value()))
                .body(e.getBody());
    }

    private ErrorMessageResponse getErrorMessageBody(
            HttpStatus status,
            MessageType type,
            Exception e
    ) {
        return ErrorMessageResponse.builder()
                .message(e.getMessage())
                .createdAd(Instant.now())
                .statusCode(status)
                .status(status.value())
                .type(type)
                .build();
    }

    private ErrorMessageResponse getErrorMessageBody(HttpStatus status, Exception e) {
        return ErrorMessageResponse.builder()
                .message(e.getMessage())
                .createdAd(Instant.now())
                .statusCode(status)
                .status(status.value())
                .type(MessageType.ERROR)
                .build();
    }

}
