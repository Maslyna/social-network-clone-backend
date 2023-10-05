package net.maslyna.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.user.client.FollowerClient;
import net.maslyna.user.client.SecurityClient;
import net.maslyna.user.exception.UserAlreadyExistsException;
import net.maslyna.user.exception.UserNotFoundException;
import net.maslyna.user.exception.UserRegistrationException;
import net.maslyna.user.exception.WrongDataException;
import net.maslyna.user.model.dto.request.SecurityRegistrationRequest;
import net.maslyna.user.model.dto.request.UserRegistrationFollowerServiceRequest;
import net.maslyna.user.model.dto.request.UserRegistrationRequest;
import net.maslyna.user.model.dto.request.UserRequest;
import net.maslyna.user.model.dto.response.AuthenticationResponse;
import net.maslyna.user.model.entity.User;
import net.maslyna.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PropertiesMessageService messageService;
    private final SecurityClient securityClient;
    private final FollowerClient followerClient;

    public AuthenticationResponse registration(UserRegistrationRequest request) {
        isEmailValid(request);
        User user = createUser(request.email());

        userFollowerServiceRegistration(user);

        return userSecurityServiceRegistration(request, user);
    }

    @Transactional(readOnly = true)
    public User getUser(Long userId) {
        if (userId == null) {
            throw new WrongDataException(
                    HttpStatus.BAD_REQUEST,
                    messageService.getProperty("validation.data.not-valid")
            );
        }
        return getUserById(userId);
    }

    @Transactional(readOnly = true)
    public Page<User> getUsers(Integer page, Integer size) {
        return userRepository.findUsersPage(PageRequest.of(page, size));
    }

    @Transactional
    public User editUser(Long userId, UserRequest userRequest) {
        return null; //TODO: edit user
    }

    private User createUser(String email) {
        return userRepository.save(
                User.builder()
                        .email(email) // TODO: random name generator
                        .createdAt(Instant.now())
                        .build()
        );
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        HttpStatus.NOT_FOUND,
                        messageService.getProperty("error.user.not-found", userId)
                ));
    }

    private boolean isResponseValid(ResponseEntity<?> response) {
        return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
    }

    private SecurityRegistrationRequest getRegistrationRequest(UserRegistrationRequest request, User user) {
        return SecurityRegistrationRequest.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(request.password())
                .build();
    }

    private void userFollowerServiceRegistration(User user) {
        ResponseEntity<Void> followerServiceResponse = followerClient.userRegistration(
                new UserRegistrationFollowerServiceRequest(user.getId())
        );
        if (!followerServiceResponse.getStatusCode().is2xxSuccessful()) {
            throw new UserRegistrationException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    messageService.getProperty("error.user.registration")
            );
        }
    }

    private AuthenticationResponse userSecurityServiceRegistration(UserRegistrationRequest request, User user) {
        ResponseEntity<AuthenticationResponse> securityServiceResponse = securityClient.register(
                getRegistrationRequest(request, user)
        );
        if (!isResponseValid(securityServiceResponse)) {
            throw new UserRegistrationException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    messageService.getProperty("error.user.registration")
            );
        }
        return securityServiceResponse.getBody();
    }

    private void isEmailValid(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.user.email.occupied", request.email())
            );
        }
    }
}
