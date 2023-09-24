package net.maslyna.userservice.mapper;

import net.maslyna.userservice.model.dto.response.UserResponse;
import net.maslyna.userservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //TODO: mappers to entities

    @Mapping(target = "email", expression = "java(user.getEmail())")
    @Mapping(target = "name", expression = "java(user.getName())")
    @Mapping(target = "createdAt", expression = "java(user.getCreatedAt())")
    @Mapping(target = "birthday", expression = "java(user.getBirthday())")
    UserResponse userToUserResponse(User user);
}
