package net.maslyna.secutiry.repository;

import net.maslyna.secutiry.model.entity.Account;
import net.maslyna.secutiry.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByAccount_Id(Long id);
    Optional<Token> findByJwt(String jwt);
    long deleteByAccount(Account account);
}
