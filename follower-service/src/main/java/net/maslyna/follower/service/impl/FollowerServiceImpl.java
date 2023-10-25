package net.maslyna.follower.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maslyna.common.service.PropertiesMessageService;
import net.maslyna.follower.exception.AccessDeniedException;
import net.maslyna.follower.exception.GlobalFollowerServiceException;
import net.maslyna.follower.exception.UserAlreadyExists;
import net.maslyna.follower.model.entity.User;
import net.maslyna.follower.repository.UserRepository;
import net.maslyna.follower.service.FollowerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowerServiceImpl implements FollowerService {
    private final UserRepository userRepository;
    private final PropertiesMessageService messageService;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseGet(() -> saveUser(userId));
    }

    @Override
    public List<User> getFollowers(Long userId) {
        return getUserById(userId).getFollowers();
    }


    @Override
    public List<User> getSubscriptions(Long userId) {
        return getUserById(userId).getSubscriptions();
    }


    @Override
    public Page<User> getUserFollowers(Long userId, PageRequest pageRequest) {
        return getFollowersPage(userId, pageRequest);
    }

    @Override
    public Page<User> getUserSubscriptions(Long userId, PageRequest pageRequest) {
        return getSubscriptionsPage(userId, pageRequest);
    }

    @Override
    public Page<User> getSubscriptions(
            Long authUserId,
            Long userId,
            PageRequest pageRequest
    ) {
        User user = getUserById(userId);
        if (!user.isPublicSubscriptions() && !authUserId.equals(userId)) {
            throw new AccessDeniedException(HttpStatus.FORBIDDEN);
        }
        return getSubscriptionsPage(user, pageRequest);
    }

    @Override
    public Page<User> getFollowers(
            Long authUserId,
            Long userId,
            PageRequest pageRequest
    ) {
        User user = getUserById(userId);
        if (!user.isPublicFollowers() && !authUserId.equals(userId)) {
            throw new AccessDeniedException(HttpStatus.FORBIDDEN);
        }
        return getFollowersPage(user, pageRequest);
    }

    @Override
    public void follow(Long authUserId, Long userId) {
        if (authUserId.equals(userId)) {
            throw new AccessDeniedException(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.user.follow.user-follow-himself")
            );
        }

        if (isUserSubscribed(authUserId, userId)) {
            throw new GlobalFollowerServiceException(
                    HttpStatus.CONFLICT,
                    messageService.getProperty("error.user.follow.user-already-subscribed")
                            .formatted(authUserId, userId)
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
        if (!isUserSubscribed(authUserId, userId)) {
            throw new GlobalFollowerServiceException(
                    HttpStatus.BAD_REQUEST,
                    messageService.getProperty("error.user.unfollow.user-not-followed")
                            .formatted(authUserId, userId)
            );
        }
        User authUser = getUserById(authUserId);
        User user = getUserById(userId);

        user.removeFollower(authUser);
        authUser.removeSubscribe(user);
    }

    @Override
    public boolean isUserSubscribed(Long authenticatedUserId, Long userId) {
        if (authenticatedUserId.equals(userId)) {
            return false;
        }
        return userRepository.isUserSubscribed(authenticatedUserId, userId);
    }

    @Override
    public boolean isUserFollowed(Long authenticatedUserId, Long userId) {
        if (authenticatedUserId.equals(userId)) {
            return false;
        }
        return userRepository.isUserFollowed(authenticatedUserId, userId);
    }

    private Page<User> getSubscriptionsPage(Long userId, PageRequest pageRequest) {
        User user = getUserById(userId);
        return new PageImpl<>(user.getSubscriptions(), pageRequest, user.getSubscriptions().size());
    }

    private Page<User> getSubscriptionsPage(User user, PageRequest pageRequest) {
        return new PageImpl<>(user.getSubscriptions(), pageRequest, user.getSubscriptions().size());
    }

    private Page<User> getFollowersPage(Long userId, PageRequest pageRequest) {
        User user = getUserById(userId);
        return new PageImpl<>(user.getFollowers(), pageRequest, user.getFollowers().size());
    }

    private Page<User> getFollowersPage(User user, PageRequest pageRequest) {
        return new PageImpl<>(user.getFollowers(), pageRequest, user.getFollowers().size());
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
