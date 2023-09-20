package net.maslyna.secutiryservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.secutiryservice.model.entity.Account;
import net.maslyna.secutiryservice.service.JwtService;
import net.maslyna.secutiryservice.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutHandlerImpl implements LogoutHandler {
    private final TokenService tokenService;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String jwt = jwtService.extractJwt(request);

        if (jwt != null && !jwt.isEmpty()) {
            Account account = tokenService.getAccount(jwt);
            tokenService.deletePreviousToken(account);

            log.info("user {} logout", account.getEmail());
        }
    }
}
