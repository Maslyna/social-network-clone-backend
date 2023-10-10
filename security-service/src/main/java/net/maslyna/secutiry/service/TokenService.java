package net.maslyna.secutiry.service;

import net.maslyna.secutiry.model.entity.Account;
import net.maslyna.secutiry.model.entity.Token;
import org.springframework.transaction.annotation.Transactional;

public interface TokenService {
    @Transactional
    Token createToken(Account account);

    @Transactional
    boolean deletePreviousToken(Account account);

    @Transactional(readOnly = true)
    Account getAccount(String jwt);
}
