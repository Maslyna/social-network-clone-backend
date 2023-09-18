package net.maslyna.secutiryservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.maslyna.secutiryservice.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${security.prefix}")
    private String prefix;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = extractJwtFromRequest(request);
        // TODO: NOT WORKING!

//        if (authHeader == null || !authHeader.startsWith(prefix)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        if (jwt != null && !jwt.isEmpty()
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = extractUserDetailsFromJwt(jwt);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = createAuthToken(request, userDetails);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
            }
        }

//        jwt = authHeader.substring(prefix.length());
//        userEmail = jwtService.extractUsername(jwt);
//
//        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
//            if (jwtService.isTokenValid(jwt, userDetails)) {
//                UsernamePasswordAuthenticationToken authenticationToken = createAuthToken(request, userDetails);
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//        }
        filterChain.doFilter(request, response);
    }

    public UserDetails extractUserDetailsFromJwt(String jwt) {
        return userDetailsService.loadUserByUsername(extractUserEmailFromJwt(jwt));
    }

    private String extractUserEmailFromJwt(String jwt) {
        return jwtService.extractUsername(jwt);
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(prefix)) {
            return authHeader.substring(prefix.length());
        }
        return null;
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
