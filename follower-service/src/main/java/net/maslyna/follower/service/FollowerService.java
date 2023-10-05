package net.maslyna.follower.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.follower.exception.AccessDeniedException;
import net.maslyna.follower.exception.UserAlreadyExists;
import net.maslyna.follower.exception.UserNotFoundException;
import net.maslyna.follower.model.entity.User;
import net.maslyna.follower.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FollowerService {
    private final UserRepository userRepository;
    private final PropertiesMessageService messageService;

    public void userRegistration(Long userId) {
        if (userRepository.existsById(userId)) {
            throw new UserAlreadyExists(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.user.already-exists", userId)
            );
        }
        userRepository.save(
                User.builder()
                        .id(userId)
                        .build()
        );
        log.info("user with id = {} was saved", userId);
    }

    @Transactional(readOnly = true)
    public Page<User> getUserFollowers(Long userId, PageRequest pageRequest) {
        return getFollowers(userId, pageRequest);
    }

    @Transactional(readOnly = true)
    public Page<User> getUserSubscriptions(Long userId, PageRequest pageRequest) {
        return getSubscriptions(userId, pageRequest);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    public boolean follow(Long authUserId, Long userId) {
        return modifyFollower(authUserId, userId, User::addFollower);
    }

    public boolean unfollow(Long authUserId, Long userId) {
        return modifyFollower(authUserId, userId, User::removeFollower);
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

    private boolean modifyFollower(Long authUserId, Long userId, BiFunction<User, User, Boolean> action) {
        if (authUserId.equals(userId)) {
            throw new AccessDeniedException(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.user.follow.user-follow-himself")
            );
        }
        User authUser = getUserById(authUserId);
        User user = getUserById(userId);
        return action.apply(user, authUser);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        HttpStatus.NOT_FOUND,
                        messageService.getProperty("user with id = %s not found", userId)
                ));
    }
}
