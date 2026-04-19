package com.banking.app.mappers;

import com.banking.app.dtos.BankDtos;
import com.banking.app.entities.Bank;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BankMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    Bank toEntity(BankDtos.CreateBankRequest request);

    @Mapping(target = "accountCount", expression = "java(bank.getAccounts() != null ? bank.getAccounts().size() : 0)")
    BankDtos.BankResponse toResponse(Bank bank);
}
