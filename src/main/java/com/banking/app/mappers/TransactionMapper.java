package com.banking.app.mappers;

import com.banking.app.dtos.TransactionDtos;
import com.banking.app.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "sourceAccountNumber", source = "sourceAccount", qualifiedByName = "getAccountNumber")
    @Mapping(target = "destinationAccountNumber", source = "destinationAccount", qualifiedByName = "getAccountNumber")
    TransactionDtos.TransactionResponse toResponse(Transaction transaction);

    @Named("getAccountNumber")
    default String getAccountNumber(Object account) {
        if (account == null) {
            return null;
        }
        // Cast sûr car nous gérons les types Account
        return ((com.banking.app.entities.Account) account).getAccountNumber();
    }
}
