package net.maslyna.secutiry.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface BasicService {
    /**
     * Extracts Basic Authentication credentials from the HttpServletRequest.
     *
     * @param request HttpServletRequest object containing the Authorization header
     * @return Extracted Basic Authentication credentials or null if not present
     */
    String extractBasic(HttpServletRequest request);

    /**
     * Extracts Basic Authentication credentials from the provided authHeader string.
     *
     * @param authHeader String containing Authorization header value
     * @return Extracted Basic Authentication credentials or null if not present
     */
    String extractBasic(String authHeader);

    /**
     * Extracts the username from the decoded Basic Authentication credentials.
     *
     * @param decoded Decoded Basic Authentication credentials
     * @return Extracted username
     */
    String extractUsername(String decoded);

    /**
     * Extracts the password from the decoded Basic Authentication credentials.
     *
     * @param decoded Decoded Basic Authentication credentials
     * @return Extracted password
     */
    String extractPassword(String decoded);

    /**
     * Validates Basic Authentication credentials against UserDetails.
     *
     * @param decoded     Decoded Basic Authentication credentials
     * @param userDetails UserDetails object containing user information
     * @return True if Basic Authentication is valid, false otherwise
     */
    boolean isBasicAuthValid(String decoded, UserDetails userDetails);

    /**
     * Creates Basic Authentication credentials from username and password
     *
     * @param username Username
     * @param password Password
     * @return Basic Authentication credentials
     */
    String generateBasicAuth(String username, String password);
}
