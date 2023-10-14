package net.maslyna.secutiry.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.secutiry.exceptions.GlobalSecurityServiceException;
import net.maslyna.secutiry.service.BasicService;
import net.maslyna.secutiry.service.JwtService;
import net.maslyna.secutiry.service.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final BasicService basicService;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null) {
            try {
                if (authHeader.startsWith(AuthenticationType.BEARER.prefix())) {
                    String jwt = jwtService.extractJwt(authHeader);

                    if (jwt != null && !jwt.isEmpty()
                            && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = tokenService.getAccount(jwt);
                        setAuthenticationToken(request, userDetails);
                    }
                } else if (authHeader.startsWith(AuthenticationType.BASIC.prefix())) {
                    String decoded = basicService.extractBasic(authHeader);

                    if (decoded != null && !decoded.isEmpty()
                            && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService
                                .loadUserByUsername(basicService.extractUsername(decoded));

                        if (basicService.isBasicAuthValid(decoded, userDetails)) {
                            setAuthenticationToken(request, userDetails);
                        }
                    }
                }
            } catch (GlobalSecurityServiceException e) {
                exceptionWriter(response, e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationToken(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = createAuthenticationToken(request, userDetails);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);
    }

    private UsernamePasswordAuthenticationToken createAuthenticationToken(
            HttpServletRequest request,
            UserDetails userDetails
    ) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    private void exceptionWriter(HttpServletResponse response, GlobalSecurityServiceException e) {
        response.setContentType("application/json");
        response.setStatus(e.getStatusCode().value());
    }
}
