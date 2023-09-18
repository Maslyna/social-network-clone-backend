package net.maslyna.secutiryservice.repository;

import net.maslyna.secutiryservice.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<Account> findByEmail(String email);
}