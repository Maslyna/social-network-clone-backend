package net.maslyna.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.user.client.SecurityClient;
import net.maslyna.user.exception.UserAlreadyExistsException;
import net.maslyna.user.exception.UserNotFoundException;
import net.maslyna.user.exception.UserRegistrationException;
import net.maslyna.user.exception.WrongDataException;
import net.maslyna.user.model.dto.request.EditUserRequest;
import net.maslyna.user.model.dto.request.SecurityRegistrationRequest;
import net.maslyna.user.model.dto.request.UserRegistrationRequest;
import net.maslyna.user.model.dto.response.AuthenticationResponse;
import net.maslyna.user.model.entity.User;
import net.maslyna.user.repository.UserRepository;
import net.maslyna.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PropertiesMessageService messageService;
    private final SecurityClient securityClient;

    @Override
    public AuthenticationResponse registration(UserRegistrationRequest request) {
        isEmailValid(request);
        User user = createUser(request.email());

        return userSecurityServiceRegistration(request, user);
    }

    @Override
    public User getUser(Long userId) {
        if (userId == null) {
            throw new WrongDataException(
                    HttpStatus.BAD_REQUEST,
                    messageService.getProperty("validation.data.not-valid")
            );
        }
        return getUserById(userId);
    }

    @Override
    public boolean isUserExists(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Page<User> getUsers(Integer page, Integer size) {
        return userRepository.findUsersPage(PageRequest.of(page, size));
    }

    @Override
    public void editUser(Long userId, EditUserRequest userRequest) {
        final User user = getUserById(userId);

        if (userRequest.name() != null)
            user.setName(userRequest.name());

        if (userRequest.nickname() != null)
            user.setNickname(userRequest.nickname());

        if (userRequest.isPublicAccount() != null)
            user.setPublicAccount(user.isPublicAccount());

        if (userRequest.bio() != null)
            user.setBio(userRequest.bio());

        if (userRequest.birthday() != null)
            user.setBirthday(userRequest.birthday());

        if (userRequest.location() != null)
            user.setLocation(userRequest.location());
    }


    private User createUser(String email) {
        return userRepository.save(
                User.builder()
                        .email(email) // TODO: random name generator
                        .createdAt(Instant.now())
                        .isPublicAccount(true)
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

    private SecurityRegistrationRequest getRegistrationRequest(UserRegistrationRequest request, User user) {
        return SecurityRegistrationRequest.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(request.password())
                .build();
    }

    private boolean isResponseValid(ResponseEntity<?> response) {
        return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
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
