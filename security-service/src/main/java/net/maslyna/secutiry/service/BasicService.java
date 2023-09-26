package net.maslyna.secutiry.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.maslyna.secutiry.config.AuthenticationType;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicService {
    private static final String PREFIX = AuthenticationType.BASIC.prefix();
    private final PasswordEncoder passwordEncoder;

    public String extractBasic(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            return authHeader.substring(PREFIX.length());
        }
        return null;
    }

    public String extractBasic(String authHeader) {
        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            return decodeBasic(authHeader.substring(PREFIX.length()));
        }
        return null;
    }

    public String extractUsername(String decoded) {
        int separatorIndex = decoded.indexOf(':');
        return decoded.substring(0, separatorIndex);
    }

    public String extractPassword(String decoded) {
        int separatorIndex = decoded.indexOf(':');
        return decoded.substring(separatorIndex + 1);
    }

    private String decodeBasic(String basic) {
        if (basic != null) {
            return new String(Base64.decode(basic));
        }
        return null;
    }

    public boolean isBasicAuthValid(String decoded, UserDetails userDetails) {
        String username = extractUsername(decoded);
        String password = extractPassword(decoded);

        return userDetails.getUsername().equals(username)
                && passwordEncoder.matches(password, userDetails.getPassword());
    }
}
