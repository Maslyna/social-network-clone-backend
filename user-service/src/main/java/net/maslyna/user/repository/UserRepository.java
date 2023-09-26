package net.maslyna.user.repository;

import net.maslyna.user.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u")
    Page<User> findUsersPage(Pageable pageable);
    boolean existsByEmail(String email);
}