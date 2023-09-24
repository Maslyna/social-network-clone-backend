package net.maslyna.userservice.repository;

import net.maslyna.userservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u")
    Page<User> findUsersPage(Pageable pageable);
    boolean existsByEmail(String email);
}