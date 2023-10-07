package net.maslyna.follower.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.follower.exception.AccessDeniedException;
import net.maslyna.follower.exception.UserAlreadyExists;
import net.maslyna.follower.model.entity.User;
import net.maslyna.follower.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FollowerService {
    private final UserRepository userRepository;
    private final PropertiesMessageService messageService;

    public Page<User> getUserFollowers(Long userId, PageRequest pageRequest) {
        return getFollowers(userId, pageRequest);
    }

    public Page<User> getUserSubscriptions(Long userId, PageRequest pageRequest) {
        return getSubscriptions(userId, pageRequest);
    }

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
                .orElseGet(() -> userRegistration(userId));
    }

    private User userRegistration(Long userId) {
        if (userRepository.existsById(userId)) {
            throw new UserAlreadyExists(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.user.already-exists", userId)
            );
        }
        User user = save(userId);
        log.info("user with id = {} was saved", userId);
        return user;
    }

    private User save(Long userId) {
        return userRepository.save(
                User.builder()
                        .id(userId)
                        .isEnabledNotifications(true)
                        .isPublicFollowers(true)
                        .isPublicSubscriptions(true)
                        .build()
        );
    }
}
