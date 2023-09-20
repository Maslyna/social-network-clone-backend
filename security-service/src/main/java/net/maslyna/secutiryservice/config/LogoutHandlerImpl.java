package net.maslyna.secutiryservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.secutiryservice.exceptions.GlobalSecurityServiceException;
import net.maslyna.secutiryservice.model.dto.response.ErrorMessageResponse;
import net.maslyna.secutiryservice.model.entity.Account;
import net.maslyna.secutiryservice.service.AccountService;
import net.maslyna.secutiryservice.service.BasicService;
import net.maslyna.secutiryservice.service.JwtService;
import net.maslyna.secutiryservice.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutHandlerImpl implements LogoutHandler {
    private final TokenService tokenService;
    private final AccountService accountService;
    private final JwtService jwtService;
    private final BasicService basicService;

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
                }

                if (authHeader.startsWith(AuthenticationType.BASIC.prefix())) {
                    log.info(authHeader);
                    String username = basicService.extractUsername(basicService.extractBasic(authHeader));

                    Account account = accountService.getAccountByEmail(username);
                    tokenService.deletePreviousToken(account);

                    log.info("user {} logout", account.getEmail());
                }
            } catch (GlobalSecurityServiceException e) {
                exceptionWriter(response);
            }
        }
    }

    private void exceptionWriter(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
