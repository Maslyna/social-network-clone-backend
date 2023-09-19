package net.maslyna.secutiryservice.mapper;

import net.maslyna.secutiryservice.model.dto.response.AccountResponse;
import net.maslyna.secutiryservice.model.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "email", expression = "java(account.getEmail())")
    @Mapping(target = "id")
    AccountResponse accountToAccountResponse(Account account);
}
