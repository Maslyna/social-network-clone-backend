package net.maslyna.secutiryservice.exceptions.handler;

import net.maslyna.secutiryservice.exceptions.GlobalSecurityServiceException;
import net.maslyna.secutiryservice.exceptions.account.AccountNotFoundException;
import net.maslyna.secutiryservice.exceptions.account.EmailOccupiedException;
import net.maslyna.secutiryservice.model.dto.MessageType;
import net.maslyna.secutiryservice.model.dto.response.ErrorMessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AccountExceptionHandler {

    @ExceptionHandler(EmailOccupiedException.class)
    public ResponseEntity<ErrorMessageResponse> handleEmailOccupied(Exception e) {
        return getErrorMessageBody(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GlobalSecurityServiceException.class)
    public ResponseEntity<ErrorMessageResponse> handleSecurityServiceException(Exception e) {
        return getErrorMessageBody(e, HttpStatus.NOT_IMPLEMENTED);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleAccountNotFound(Exception e) {
        return getErrorMessageBody(e, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorMessageResponse> getErrorMessageBody(Exception e, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(
                        ErrorMessageResponse.builder()
                                .type(MessageType.ERROR.name())
                                .title(status)
                                .statusCode(status.value())
                                .message(e.getMessage())
                                .build()
                );
    }


}
