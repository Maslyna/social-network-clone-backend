package net.maslyna.user.mapper;

import net.maslyna.user.model.dto.response.UserResponse;
import net.maslyna.user.model.entity.User;

public interface UserMapper {

    UserResponse userToUserResponse(User user);

    UserResponse userToUserResponse(User user, Long authUserId);
}
