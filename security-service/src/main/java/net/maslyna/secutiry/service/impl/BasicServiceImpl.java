package net.maslyna.secutiry.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import net.maslyna.secutiry.config.AuthenticationType;
import net.maslyna.secutiry.service.BasicService;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicServiceImpl implements BasicService {
    private static final String PREFIX = AuthenticationType.BASIC.prefix();
    private final PasswordEncoder passwordEncoder;

    /**
     * Extracts Basic Authentication credentials from the HttpServletRequest.
     *
     * @param request HttpServletRequest object containing the Authorization header
     * @return Extracted Basic Authentication credentials or null if not present
     */
    @Override public String extractBasic(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            return authHeader.substring(PREFIX.length());
        }
        return null;
    }

    /**
     * Extracts Basic Authentication credentials from the provided authHeader string.
     *
     * @param authHeader String containing Authorization header value
     * @return Extracted Basic Authentication credentials or null if not present
     */
    @Override public String extractBasic(String authHeader) {
        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            return decodeBasic(authHeader.substring(PREFIX.length()));
        }
        return null;
    }

    /**
     * Extracts the username from the decoded Basic Authentication credentials.
     *
     * @param decoded Decoded Basic Authentication credentials
     * @return Extracted username
     */
    @Override public String extractUsername(String decoded) {
        int separatorIndex = decoded.indexOf(':');
        return decoded.substring(0, separatorIndex);
    }

    /**
     * Extracts the password from the decoded Basic Authentication credentials.
     *
     * @param decoded Decoded Basic Authentication credentials
     * @return Extracted password
     */
    @Override public String extractPassword(String decoded) {
        int separatorIndex = decoded.indexOf(':');
        return decoded.substring(separatorIndex + 1);
    }

    /**
     * Validates Basic Authentication credentials against UserDetails.
     *
     * @param decoded     Decoded Basic Authentication credentials
     * @param userDetails UserDetails object containing user information
     * @return True if Basic Authentication is valid, false otherwise
     */
    @Override public boolean isBasicAuthValid(String decoded, UserDetails userDetails) {
        String username = extractUsername(decoded);
        String password = extractPassword(decoded);

        return userDetails.getUsername().equals(username)
                && passwordEncoder.matches(password, userDetails.getPassword());
    }

    /**
     * Creates Basic Authentication credentials from username and password
     * @param username Username
     * @param password Password
     * @return Basic Authentication credentials
     */
    @Override public String generateBasicAuth(String username, String password) {
        String credentials = username + ":" + password;
        byte[] credentialsBytes = credentials.getBytes();
        String encodedCredentials = new String(Base64.encode(credentialsBytes));

        return PREFIX + encodedCredentials;
    }

    /**
     * Decodes the Base64-encoded Basic Authentication string.
     *
     * @param basic Base64-encoded Basic Authentication string
     * @return Decoded Basic Authentication string
     */
    private String decodeBasic(String basic) {
        if (basic != null) {
            return new String(Base64.decode(basic));
        }
        return null;
    }
}
