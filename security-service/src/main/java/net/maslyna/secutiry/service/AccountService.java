package net.maslyna.secutiry.service;

import net.maslyna.secutiry.model.entity.Account;
import org.springframework.transaction.annotation.Transactional;

public interface AccountService {
    @Transactional(readOnly = true)
    Account getAccountByEmail(String email);

    @Transactional
    Account createUserAccount(Long id, String email, String password);

    @Transactional(readOnly = true)
    boolean isUserAlreadyExists(Long id, String email);

    @Transactional(readOnly = true)
    boolean isUserAlreadyExists(String email);
}
