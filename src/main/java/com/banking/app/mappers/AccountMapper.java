package com.banking.app.mappers;

import com.banking.app.dtos.AccountDtos;
import com.banking.app.entities.Account;
import com.banking.app.entities.Bank;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "bank", source = "bank")
    AccountDtos.AccountResponse toResponse(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "balance", source = "request.initialBalance")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "bank", ignore = true)
    Account toEntity(AccountDtos.CreateAccountRequest request, @Context Bank bank);

    @AfterMapping
    default void setBank(@MappingTarget Account account, @Context Bank bank) {
        account.setBank(bank);
    }

    default AccountDtos.BankSummary toBankSummary(Bank bank) {
        if (bank == null) return null;
        return new AccountDtos.BankSummary(bank.getId(), bank.getName(), bank.getCode(), bank.getCountry());
    }
}
