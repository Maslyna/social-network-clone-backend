package net.maslyna.user.mapper.impl;

import net.maslyna.user.mapper.UserMapper;
import net.maslyna.user.model.dto.response.UserResponse;
import net.maslyna.user.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserResponse userToUserResponse(User user) {
        return user.isPublicAccount()
                ? getResponseForPublicAccount(user)
                : getResponseForPrivateAccount(user);
    }

    @Override
    public UserResponse userToUserResponse(User user, Long authUserId) {
        return user.isPublicAccount() || user.getId().equals(authUserId)
                ? getResponseForPublicAccount(user)
                : getResponseForPrivateAccount(user);
    }

    private UserResponse getResponseForPublicAccount(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .imageUrl(user.getProfilePhoto() != null ? user.getProfilePhoto().getImageUrl() : null)
                .birthday(user.getBirthday())
                .location(user.getLocation())
                .bio(user.getBio())
                .build();
    }

    private UserResponse getResponseForPrivateAccount(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
