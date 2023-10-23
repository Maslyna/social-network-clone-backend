package net.maslyna.follower.repository;

import net.maslyna.follower.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select (count(u) > 0) from User u inner join u.subscriptions subscriptions where u.id = ?1 and subscriptions.id = ?2")
    boolean isUserSubscribed(Long user, Long subscribed);

    @Query("select (count(u) > 0) from User u inner join u.followers followers where u.id = ?1 and followers.id = ?2")
    boolean isUserFollowed(Long user, Long follower);
}