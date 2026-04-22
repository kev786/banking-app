package com.banking.app.services;

import com.banking.app.dtos.TransactionDtos;
import com.banking.app.entities.Account;
import com.banking.app.entities.Bank;
import com.banking.app.entities.Transaction;
import com.banking.app.enums.AccountStatus;
import com.banking.app.enums.TransactionType;
import com.banking.app.exceptions.AccountInactiveException;
import com.banking.app.exceptions.InsufficientFundsException;
import com.banking.app.exceptions.ResourceNotFoundException;
import com.banking.app.mappers.TransactionMapper;
import com.banking.app.repositories.AccountRepository;
import com.banking.app.repositories.TransactionRepository;
import com.banking.app.services.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Bank testBank;
    private Account sourceAccount;
    private Account destinationAccount;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        testBank = Bank.builder()
                .id(1L)
                .name("Test Bank")
                .code("TB")
                .build();

        sourceAccount = Account.builder()
                .id(1L)
                .accountNumber("TB0000000001")
                .ownerName("Alice")
                .ownerEmail("alice@example.com")
                .balance(BigDecimal.valueOf(5000))
                .status(AccountStatus.ACTIVE)
                .bank(testBank)
                .build();

        destinationAccount = Account.builder()
                .id(2L)
                .accountNumber("TB0000000002")
                .ownerName("Bob")
                .ownerEmail("bob@example.com")
                .balance(BigDecimal.valueOf(1000))
                .status(AccountStatus.ACTIVE)
                .bank(testBank)
                .build();

        testTransaction = Transaction.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(100))
                .fee(BigDecimal.ZERO)
                .type(TransactionType.TRANSFER)
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .build();
    }

    @Test
    void testDeposit_Success() {
        TransactionDtos.DepositRequest request = new TransactionDtos.DepositRequest(
                1L,
                BigDecimal.valueOf(500),
                "Deposit test"
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        when(transactionMapper.toResponse(any())).thenReturn(
                new TransactionDtos.TransactionResponse(1L, BigDecimal.valueOf(500), BigDecimal.ZERO,
                        TransactionType.DEPOSIT, "Deposit test", null, null, null)
        );

        TransactionDtos.TransactionResponse response = transactionService.deposit(request);

        assertNotNull(response);
        assertEquals(TransactionType.DEPOSIT, response.type());
        verify(accountRepository, times(1)).save(sourceAccount);
        assertEquals(BigDecimal.valueOf(5500), sourceAccount.getBalance());
    }

    @Test
    void testDeposit_AccountInactive() {
        sourceAccount.setStatus(AccountStatus.INACTIVE);
        TransactionDtos.DepositRequest request = new TransactionDtos.DepositRequest(
                1L,
                BigDecimal.valueOf(500),
                "Deposit test"
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));

        assertThrows(AccountInactiveException.class, () -> transactionService.deposit(request));
    }

    @Test
    void testWithdraw_Success() {
        TransactionDtos.WithdrawRequest request = new TransactionDtos.WithdrawRequest(
                1L,
                BigDecimal.valueOf(500),
                "Withdraw test"
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        when(transactionMapper.toResponse(any())).thenReturn(
                new TransactionDtos.TransactionResponse(1L, BigDecimal.valueOf(500), BigDecimal.ZERO,
                        TransactionType.WITHDRAWAL, "Withdraw test", null, null, null)
        );

        TransactionDtos.TransactionResponse response = transactionService.withdraw(request);

        assertNotNull(response);
        assertEquals(TransactionType.WITHDRAWAL, response.type());
        assertEquals(BigDecimal.valueOf(4500), sourceAccount.getBalance());
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        sourceAccount.setBalance(BigDecimal.valueOf(100));
        TransactionDtos.WithdrawRequest request = new TransactionDtos.WithdrawRequest(
                1L,
                BigDecimal.valueOf(500),
                "Withdraw test"
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));

        assertThrows(InsufficientFundsException.class, () -> transactionService.withdraw(request));
    }

    @Test
    void testTransfer_Success() {
        TransactionDtos.TransferRequest request = new TransactionDtos.TransferRequest(
                1L,
                2L,
                BigDecimal.valueOf(1000),
                "Transfer test"
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);
        when(transactionMapper.toResponse(any())).thenReturn(
                new TransactionDtos.TransactionResponse(1L, BigDecimal.valueOf(1000), BigDecimal.ZERO,
                        TransactionType.TRANSFER, "Transfer test", null, "TB0000000001", "TB0000000002")
        );

        TransactionDtos.TransactionResponse response = transactionService.transfer(request);

        assertNotNull(response);
        assertEquals(TransactionType.TRANSFER, response.type());
        assertEquals(BigDecimal.valueOf(4000), sourceAccount.getBalance());
        assertEquals(BigDecimal.valueOf(2000), destinationAccount.getBalance());
    }

    @Test
    void testTransfer_InsufficientFunds() {
        sourceAccount.setBalance(BigDecimal.valueOf(500));
        TransactionDtos.TransferRequest request = new TransactionDtos.TransferRequest(
                1L,
                2L,
                BigDecimal.valueOf(1000),
                "Transfer test"
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));

        assertThrows(InsufficientFundsException.class, () -> transactionService.transfer(request));
    }

    @Test
    void testTransfer_SourceAccountNotFound() {
        TransactionDtos.TransferRequest request = new TransactionDtos.TransferRequest(
                1L,
                2L,
                BigDecimal.valueOf(1000),
                "Transfer test"
        );

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transactionService.transfer(request));
    }

    @Test
    void testGetAccountHistory() {
        List<Transaction> transactions = List.of(testTransaction);
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findAllByAccountId(1L)).thenReturn(transactions);
        when(transactionMapper.toResponse(testTransaction)).thenReturn(
                new TransactionDtos.TransactionResponse(1L, BigDecimal.valueOf(100), BigDecimal.ZERO,
                        TransactionType.TRANSFER, null, null, "TB0000000001", "TB0000000002")
        );

        List<TransactionDtos.TransactionResponse> responses = transactionService.getAccountHistory(1L);

        assertEquals(1, responses.size());
        verify(transactionRepository, times(1)).findAllByAccountId(1L);
    }

    @Test
    void testGetAccountHistory_AccountNotFound() {
        when(accountRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getAccountHistory(1L));
    }
}
