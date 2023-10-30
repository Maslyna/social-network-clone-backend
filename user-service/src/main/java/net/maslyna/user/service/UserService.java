package net.maslyna.user.service;

import net.maslyna.user.model.dto.request.EditUserRequest;
import net.maslyna.user.model.dto.request.UserRegistrationRequest;
import net.maslyna.user.model.dto.response.AuthenticationResponse;
import net.maslyna.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    AuthenticationResponse registration(UserRegistrationRequest request);

    @Transactional(readOnly = true)
    User getUser(Long userId);

    @Transactional(readOnly = true)
    Page<User> getUsers(Integer page, Integer size);

    @Transactional
    User editUser(Long userId, EditUserRequest userRequest);

}
