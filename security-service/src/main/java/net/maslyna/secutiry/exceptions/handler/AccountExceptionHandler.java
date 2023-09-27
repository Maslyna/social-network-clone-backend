package net.maslyna.secutiry.exceptions.handler;

import net.maslyna.common.message.MessageType;
import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;
import net.maslyna.secutiry.exceptions.account.AccountNotFoundException;
import net.maslyna.secutiry.exceptions.account.AuthenticationException;
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
    public ResponseEntity<ErrorMessageResponse> handleSecurityServiceException(
            Exception e
    ) {
        return ResponseEntity.status(NOT_IMPLEMENTED).body(
                getErrorMessageBody(NOT_IMPLEMENTED, MessageType.ERROR, e)
        );
    }

    @ExceptionHandler(EmailOccupiedException.class)
    public ResponseEntity<ErrorMessageResponse> handleEmailOccupied(Exception e) {
        return ResponseEntity.status(CONFLICT).body(
                getErrorMessageBody(CONFLICT, MessageType.ERROR, e)
        );
    }

    @ExceptionHandler(TokenNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotValidToken(Exception e) {
        return ResponseEntity.status(BAD_REQUEST).body(
                getErrorMessageBody(BAD_REQUEST, MessageType.ERROR, e)
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorMessageResponse> handleAuthException(Exception e) {
        return ResponseEntity.status(UNAUTHORIZED).body(
                getErrorMessageBody(UNAUTHORIZED, e)
        );
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleAccountNotFound(Exception e) {
        return ResponseEntity.status(NOT_FOUND).body(
                getErrorMessageBody(NOT_FOUND, e)
        );
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
