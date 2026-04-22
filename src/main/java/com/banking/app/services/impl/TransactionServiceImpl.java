package com.banking.app.services.impl;

import com.banking.app.dtos.TransactionDtos;
import com.banking.app.entities.Account;
import com.banking.app.entities.Transaction;
import com.banking.app.enums.AccountStatus;
import com.banking.app.enums.TransactionType;
import com.banking.app.exceptions.AccountInactiveException;
import com.banking.app.exceptions.InsufficientFundsException;
import com.banking.app.exceptions.ResourceNotFoundException;
import com.banking.app.mappers.TransactionMapper;
import com.banking.app.repositories.AccountRepository;
import com.banking.app.repositories.TransactionRepository;
import com.banking.app.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionDtos.TransactionResponse deposit(TransactionDtos.DepositRequest request) {
        Account account = findAccountById(request.accountId());
        validateAccountActive(account);

        account.setBalance(account.getBalance().add(request.amount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .fee(BigDecimal.ZERO)
                .type(TransactionType.DEPOSIT)
                .description(request.description())
                .destinationAccount(account)
                .build();

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Override
    public TransactionDtos.TransactionResponse withdraw(TransactionDtos.WithdrawRequest request) {
        Account account = findAccountById(request.accountId());
        validateAccountActive(account);

        if (account.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Solde insuffisant pour effectuer le retrait");
        }

        account.setBalance(account.getBalance().subtract(request.amount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .fee(BigDecimal.ZERO)
                .type(TransactionType.WITHDRAWAL)
                .description(request.description())
                .sourceAccount(account)
                .build();

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Override
    public TransactionDtos.TransactionResponse transfer(TransactionDtos.TransferRequest request) {
        Account sourceAccount = findAccountById(request.sourceAccountId());
        Account destinationAccount = findAccountById(request.destinationAccountId());

        validateAccountActive(sourceAccount);
        validateAccountActive(destinationAccount);

        if (sourceAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Solde insuffisant pour effectuer le virement");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.amount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.amount()));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .fee(BigDecimal.ZERO)
                .type(TransactionType.TRANSFER)
                .description(request.description())
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .build();

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDtos.TransactionResponse> getAccountHistory(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("Compte non trouvé");
        }
        return transactionRepository.findAllByAccountId(accountId).stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compte non trouvé avec l'ID: " + id));
    }

    private void validateAccountActive(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountInactiveException("Le compte est " + account.getStatus().name().toLowerCase() + " et ne peut pas effectuer d'opérations");
        }
    }
}
