package net.maslyna.userservice.repository;

import net.maslyna.userservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}