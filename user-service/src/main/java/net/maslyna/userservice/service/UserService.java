package net.maslyna.userservice.service;

import net.maslyna.userservice.model.dto.request.UserRegistrationRequest;
import net.maslyna.userservice.model.dto.response.UserResponse;
import net.maslyna.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public record UserService (
        UserRepository userRepository
) {
    public UserResponse registration(UserRegistrationRequest request) {
        // TODO: auth registration
        // TODO: validation
        return null;
    }
}
