package net.maslyna.secutiry.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.secutiry.config.AuthenticationType;
import net.maslyna.secutiry.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private static final String PREFIX = AuthenticationType.BEARER.prefix();
    @Value("${security.key}")
    private String SECRET_KEY;

    @PostConstruct
    void logging() {
        log.info("security.key = {}", SECRET_KEY);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String extractJwt(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            return authHeader.substring(PREFIX.length());
        }
        return null;
    }

    @Override
    public String extractJwt(String authHeader) {
        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            return authHeader.substring(PREFIX.length());
        }
        return null;
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return userDetails.getUsername().equals(username) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    @Override
    public String generateToken(
            UserDetails userDetails
    ) {
        return generateToken(userDetails, Map.of());
    }

    @Override
    public String generateToken(
            UserDetails userDetails,
            Map<String, Object> extraClaims
    ) {
        log.info("token generation for user {}, claims {}", userDetails.getUsername(), extraClaims);
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token) // parseClaimsJwt() is incorrect method
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
