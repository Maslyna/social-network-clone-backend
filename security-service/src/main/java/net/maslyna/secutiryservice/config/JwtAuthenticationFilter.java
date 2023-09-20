package net.maslyna.secutiryservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.maslyna.secutiryservice.exceptions.GlobalSecurityServiceException;
import net.maslyna.secutiryservice.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwt = jwtService.extractJwt(request);

        if (jwt != null && !jwt.isEmpty()
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = extractUserDetailsFromJwt(jwt);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = createAuthToken(request, userDetails);
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(context);
                }
            } catch (GlobalSecurityServiceException e) {
                exceptionWriter(response);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void exceptionWriter(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private UserDetails extractUserDetailsFromJwt(String jwt) {
        return userDetailsService.loadUserByUsername(extractUserEmailFromJwt(jwt));
    }

    private String extractUserEmailFromJwt(String jwt) {
        return jwtService.extractUsername(jwt);
    }


    private UsernamePasswordAuthenticationToken createAuthToken(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }
}
