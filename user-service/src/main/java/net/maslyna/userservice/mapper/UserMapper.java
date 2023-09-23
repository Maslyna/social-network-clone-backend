package net.maslyna.userservice.mapper;

import net.maslyna.userservice.model.dto.response.UserResponse;
import net.maslyna.userservice.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //TODO: mappers to entities
    UserResponse userToUserResponse(User user);
}
