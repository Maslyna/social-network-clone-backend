package net.maslyna.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.user.client.SecurityClient;
import net.maslyna.user.exception.*;
import net.maslyna.user.mapper.UserMapper;
import net.maslyna.user.model.dto.request.EditUserRequest;
import net.maslyna.user.model.dto.request.SecurityRegistrationRequest;
import net.maslyna.user.model.dto.request.UserRegistrationRequest;
import net.maslyna.user.model.dto.response.AuthenticationResponse;
import net.maslyna.user.model.dto.response.UserRegistrationResponse;
import net.maslyna.user.model.dto.response.UserResponse;
import net.maslyna.user.model.entity.User;
import net.maslyna.user.repository.UserRepository;
import net.maslyna.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PropertiesMessageService messageService;
    private final SecurityClient securityClient;

    @Override
    public UserRegistrationResponse registration(UserRegistrationRequest request) {
        isEmailValid(request.email());
        User user = createUser(request.email());

        AuthenticationResponse authenticationResponse = userSecurityServiceRegistration(request, user);
        return UserRegistrationResponse.builder().id(user.getId()).token(authenticationResponse.token()).build();
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

        if (userRepository.existsByEmail(userRequest.email())) {
            throw new GlobalUserServiceException(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.user.email.occupied")
            );
        }

        if (userRequest.name() != null)
            user.setName(userRequest.name());

        if (userRequest.nickname() != null)
            user.setNickname(userRequest.nickname());

        if (userRequest.isPublicAccount() != null)
            user.setPublicAccount(userRequest.isPublicAccount());

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
                        .userPhotos(new ArrayList<>())
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

    private boolean isResponseValid(ResponseEntity<?> response) {
        return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
    }

    private void isEmailValid(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.user.email.occupied", email)
            );
        }
    }
}
