package net.maslyna.secutiryservice.repository;

import net.maslyna.secutiryservice.model.entity.Account;
import net.maslyna.secutiryservice.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByJwt(String jwt);
    long deleteByAccount(Account account);
}
