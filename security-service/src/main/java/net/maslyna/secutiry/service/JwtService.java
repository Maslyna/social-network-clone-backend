package net.maslyna.secutiry.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);

    String extractJwt(HttpServletRequest request);

    String extractJwt(String authHeader);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    String generateToken(
            UserDetails userDetails
    );

    String generateToken(
            UserDetails userDetails,
            Map<String, Object> extraClaims
    );
}
