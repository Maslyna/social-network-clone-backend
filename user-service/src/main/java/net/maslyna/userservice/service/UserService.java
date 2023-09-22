package net.maslyna.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.userservice.model.dto.request.UserRegistrationRequest;
import net.maslyna.userservice.model.dto.response.UserResponse;
import net.maslyna.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    public UserResponse registration(UserRegistrationRequest request) {
        // TODO: auth registration
        // TODO: validation
        return null;
    }
}
