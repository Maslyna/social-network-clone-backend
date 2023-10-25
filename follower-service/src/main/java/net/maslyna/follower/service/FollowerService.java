package net.maslyna.follower.service;

import net.maslyna.follower.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional
public interface FollowerService {

    User getUserById(Long userId);

    List<User> getFollowers(Long userId);

    List<User> getSubscriptions(Long userId);

    Page<User> getUserFollowers(Long userId, PageRequest pageRequest);

    Page<User> getUserSubscriptions(Long userId, PageRequest pageRequest);

    Page<User> getSubscriptions(
            Long authUserId,
            Long userId,
            PageRequest pageRequest
    );

    Page<User> getFollowers(
            Long authUserId,
            Long userId,
            PageRequest pageRequest
    );

    void follow(Long authUserId, Long userId);

    void unfollow(Long authUserId, Long userId);

    boolean isUserSubscribed(Long authenticatedUserId, Long userId);

    boolean isUserFollowed(Long authenticatedUserId, Long userId);
}
