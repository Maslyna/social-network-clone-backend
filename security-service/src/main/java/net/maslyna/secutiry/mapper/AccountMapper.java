package net.maslyna.secutiry.mapper;

import net.maslyna.secutiry.model.dto.response.AccountResponse;
import net.maslyna.secutiry.model.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "email", expression = "java(account.getEmail())")
    @Mapping(target = "id")
    AccountResponse accountToAccountResponse(Account account);
}
