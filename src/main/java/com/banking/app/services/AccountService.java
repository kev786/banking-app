package com.banking.app.services;

import com.banking.app.dtos.AccountDtos;

import java.util.List;

public interface AccountService {

    AccountDtos.AccountResponse createAccount(AccountDtos.CreateAccountRequest request);

    List<AccountDtos.AccountResponse> getAllAccounts();

    List<AccountDtos.AccountResponse> getAccountsByBank(Long bankId);

    AccountDtos.AccountResponse getAccountById(Long id);
    void deleteAccount(Long id);
}
