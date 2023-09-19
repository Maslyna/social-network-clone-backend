package net.maslyna.secutiryservice.repository;

import net.maslyna.secutiryservice.model.entity.Account;
import net.maslyna.secutiryservice.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByJwt(String jwt);
    long deleteByAccount(Account account);
    long deleteByJwt(String jwt);
}
