package com.banking.app.services;

import com.banking.app.dtos.TransactionDtos;
import java.util.List;

public interface TransactionService {
    TransactionDtos.TransactionResponse deposit(TransactionDtos.DepositRequest request);
    TransactionDtos.TransactionResponse withdraw(TransactionDtos.WithdrawRequest request);
    TransactionDtos.TransactionResponse transfer(TransactionDtos.TransferRequest request);
    List<TransactionDtos.TransactionResponse> getAccountHistory(Long accountId);
}
