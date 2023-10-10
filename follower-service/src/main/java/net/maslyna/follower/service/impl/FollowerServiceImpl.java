package net.maslyna.follower.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.follower.exception.AccessDeniedException;
import net.maslyna.follower.exception.UserAlreadyExists;
import net.maslyna.follower.model.entity.User;
import net.maslyna.follower.repository.UserRepository;
import net.maslyna.follower.service.FollowerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FollowerServiceImpl implements FollowerService {
    private final UserRepository userRepository;
    private final PropertiesMessageService messageService;

    @Override
    public Page<User> getUserFollowers(Long userId, PageRequest pageRequest) {
        return getFollowers(userId, pageRequest);
    }

    @Override
    public Page<User> getUserSubscriptions(Long userId, PageRequest pageRequest) {
        return getSubscriptions(userId, pageRequest);
    }

    @Override
    public Page<User> getUserSubscriptions(
            Long authUserId,
            Long userId,
            PageRequest pageRequest) {
        User user = getUserById(userId);
        if (!user.isPublicSubscriptions() && !authUserId.equals(userId)) {
            throw new AccessDeniedException(HttpStatus.FORBIDDEN);
        }
        return getSubscriptions(user, pageRequest);
    }

    @Override
    public Page<User> getUserFollowers(
            Long authUserId,
            Long userId,
            PageRequest pageRequest) {
        User user = getUserById(userId);
        if (!user.isPublicFollowers() && !authUserId.equals(userId)) {
            throw new AccessDeniedException(HttpStatus.FORBIDDEN);
        }
        return getFollowers(user, pageRequest);
    }

    @Override
    public void follow(Long authUserId, Long userId) {
        if (authUserId.equals(userId)) {
            throw new AccessDeniedException(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.user.follow.user-follow-himself")
            );
        }
        User authUser = getUserById(authUserId);
        User user = getUserById(userId);

        authUser.addSubscribe(user);
        user.addFollower(authUser);
    }

    @Override
    public void unfollow(Long authUserId, Long userId) {
        if (authUserId.equals(userId)) {
            throw new AccessDeniedException(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.user.follow.user-follow-himself")
            );
        }
        User authUser = getUserById(authUserId);
        User user = getUserById(userId);

        user.removeFollower(authUser);
        authUser.removeSubscribe(user);
    }

    private Page<User> getSubscriptions(Long userId, PageRequest pageRequest) {
        User user = getUserById(userId);
        return new PageImpl<>(user.getSubscriptions(), pageRequest, user.getSubscriptions().size());
    }

    private Page<User> getSubscriptions(User user, PageRequest pageRequest) {
        return new PageImpl<>(user.getSubscriptions(), pageRequest, user.getSubscriptions().size());
    }

    private Page<User> getFollowers(Long userId, PageRequest pageRequest) {
        User user = getUserById(userId);
        return new PageImpl<>(user.getFollowers(), pageRequest, user.getFollowers().size());
    }

    private Page<User> getFollowers(User user, PageRequest pageRequest) {
        return new PageImpl<>(user.getFollowers(), pageRequest, user.getFollowers().size());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseGet(() -> saveUser(userId));
    }

    private User saveUser(Long userId) {
        if (userRepository.existsById(userId)) {
            throw new UserAlreadyExists(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.user.already-exists", userId)
            );
        }
        User user = userRepository.save(
                User.builder()
                        .id(userId)
                        .followers(new ArrayList<>())
                        .subscriptions(new ArrayList<>())
                        .isEnabledNotifications(true)
                        .isPublicFollowers(true)
                        .isPublicSubscriptions(true)
                        .build()
        );
        log.info("User with id = {} was saved", userId);
        return user;
    }
}
