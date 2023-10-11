package net.maslyna.follower.service;

import net.maslyna.follower.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface FollowerService {

    Page<User> getUserFollowers(Long userId, PageRequest pageRequest);

    Page<User> getUserSubscriptions(Long userId, PageRequest pageRequest);

    Page<User> getUserSubscriptions(
            Long authUserId,
            Long userId,
            PageRequest pageRequest);

    Page<User> getUserFollowers(
            Long authUserId,
            Long userId,
            PageRequest pageRequest);

    void follow(Long authUserId, Long userId);

    void unfollow(Long authUserId, Long userId);
}
