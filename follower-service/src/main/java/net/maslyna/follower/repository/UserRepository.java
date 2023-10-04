package net.maslyna.follower.repository;

import net.maslyna.follower.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}