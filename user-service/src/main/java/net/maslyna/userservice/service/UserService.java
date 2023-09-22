package net.maslyna.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.userservice.client.SecurityClient;
import net.maslyna.userservice.exceptions.UserAlreadyExistsException;
import net.maslyna.userservice.exceptions.UserRegistrationException;
import net.maslyna.userservice.model.dto.request.UserRegistrationRequest;
import net.maslyna.userservice.model.dto.response.AuthenticationResponse;
import net.maslyna.userservice.model.dto.response.UserResponse;
import net.maslyna.userservice.model.entity.User;
import net.maslyna.userservice.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PropertiesMessageService messageService;
    private final SecurityClient securityClient;

    public AuthenticationResponse registration(UserRegistrationRequest request) {
        // TODO: auth registration
        // TODO: validation
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                    messageService.getProperty("error.user.email.occupied")
            );
        }
        User user = createUser(request.email());
        ResponseEntity<AuthenticationResponse> response = securityClient.register(request);
        if (!validateResponse(response)) {
            throw new UserRegistrationException(
                    messageService.getProperty("error.user.registration")
            );
        }
        return response.getBody();
    }

    private User createUser(String email) {
        return userRepository.save(
                User.builder()
                        .email(email) // TODO: random name generator
                        .createdAt(Instant.now())
                        .build()
        );
    }

    private boolean validateResponse(ResponseEntity<?> response) {
        return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
    }
}
