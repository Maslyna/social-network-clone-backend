package net.maslyna.secutiry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.message.MessageType;
import net.maslyna.common.response.ErrorMessageResponse;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.secutiry.exceptions.AuthenticationException;
import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;
import net.maslyna.secutiry.model.entity.Account;
import net.maslyna.secutiry.service.AccountService;
import net.maslyna.secutiry.service.BasicService;
import net.maslyna.secutiry.service.JwtService;
import net.maslyna.secutiry.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutHandlerImpl implements LogoutHandler {
    private final TokenService tokenService;
    private final AccountService accountService;
    private final JwtService jwtService;
    private final BasicService basicService;
    private final PropertiesMessageService messageService;
    private final ObjectMapper objectMapper;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            try {
                if (authHeader.startsWith(AuthenticationType.BEARER.prefix())) {
                    String jwt = jwtService.extractJwt(authHeader);
                    Account account = tokenService.getAccount(jwt);
                    tokenService.deletePreviousToken(account);

                    log.info("user {} logout", account.getEmail());
                } else if (authHeader.startsWith(AuthenticationType.BASIC.prefix())) {
                    log.info(authHeader);
                    String username = basicService.extractUsername(basicService.extractBasic(authHeader));

                    Account account = accountService.getAccountByEmail(username);
                    tokenService.deletePreviousToken(account);

                    log.info("user {} logout", account.getEmail());
                } else {
                    throw new AuthenticationException(
                            HttpStatus.NOT_ACCEPTABLE,
                            messageService.getProperty("error.authentication.not-supported-type")
                    );
                }
            } catch (GlobalSecurityServiceException e) {
                exceptionWriter(response, e);
            }
        }
    }

    private void exceptionWriter(HttpServletResponse response, GlobalSecurityServiceException e) {
        response.setContentType("application/json");
        response.setStatus(e.getBody().getStatus());
        ErrorMessageResponse error = ErrorMessageResponse.builder()
                .createdAt(Instant.now())
                .statusCode((HttpStatus) e.getStatusCode())
                .status(e.getStatusCode().value())
                .type(MessageType.ERROR)
                .message(e.getReason())
                .build();
        try {
            response.getWriter().write(objectMapper.writeValueAsString(error));
        } catch (IOException ignored) {

        }
    }
}
