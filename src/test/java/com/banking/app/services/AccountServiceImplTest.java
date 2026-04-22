package com.banking.app.services;

import com.banking.app.dtos.AccountDtos;
import com.banking.app.entities.Account;
import com.banking.app.entities.Bank;
import com.banking.app.enums.AccountStatus;
import com.banking.app.enums.AccountType;
import com.banking.app.exceptions.ResourceNotFoundException;
import com.banking.app.mappers.AccountMapper;
import com.banking.app.repositories.AccountRepository;
import com.banking.app.repositories.BankRepository;
import com.banking.app.services.impl.AccountServiceImpl;
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
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private BankRepository bankRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Bank testBank;
    private Account testAccount;
    private AccountDtos.CreateAccountRequest createRequest;

    @BeforeEach
    void setUp() {
        testBank = Bank.builder()
                .id(1L)
                .name("Test Bank")
                .code("TB")
                .address("123 Main St")
                .country("Cameroon")
                .build();

        testAccount = Account.builder()
                .id(1L)
                .accountNumber("TB0000000001")
                .ownerName("John Doe")
                .ownerEmail("john@example.com")
                .balance(BigDecimal.valueOf(1000))
                .accountType(AccountType.CHECKING)
                .status(AccountStatus.ACTIVE)
                .bank(testBank)
                .build();

        createRequest = new AccountDtos.CreateAccountRequest(
                "Jane Doe",
                "jane@example.com",
                BigDecimal.valueOf(500),
                AccountType.SAVINGS,
                1L
        );
    }

    @Test
    void testCreateAccount_Success() {
        when(bankRepository.findById(1L)).thenReturn(Optional.of(testBank));
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(accountMapper.toEntity(createRequest, testBank)).thenReturn(testAccount);
        when(accountMapper.toResponse(testAccount)).thenReturn(
                new AccountDtos.AccountResponse(1L, "TB0000000001", "John Doe", "john@example.com",
                        BigDecimal.valueOf(1000), AccountType.CHECKING, AccountStatus.ACTIVE, null, null, null)
        );

        AccountDtos.AccountResponse response = accountService.createAccount(createRequest);

        assertNotNull(response);
        assertEquals("TB0000000001", response.accountNumber());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateAccount_BankNotFound() {
        when(bankRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.createAccount(createRequest));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void testGetAllAccounts() {
        List<Account> accounts = List.of(testAccount);
        when(accountRepository.findAllWithBank()).thenReturn(accounts);
        when(accountMapper.toResponse(testAccount)).thenReturn(
                new AccountDtos.AccountResponse(1L, "TB0000000001", "John Doe", "john@example.com",
                        BigDecimal.valueOf(1000), AccountType.CHECKING, AccountStatus.ACTIVE, null, null, null)
        );

        List<AccountDtos.AccountResponse> responses = accountService.getAllAccounts();

        assertEquals(1, responses.size());
        verify(accountRepository, times(1)).findAllWithBank();
    }

    @Test
    void testGetAccountById_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountMapper.toResponse(testAccount)).thenReturn(
                new AccountDtos.AccountResponse(1L, "TB0000000001", "John Doe", "john@example.com",
                        BigDecimal.valueOf(1000), AccountType.CHECKING, AccountStatus.ACTIVE, null, null, null)
        );

        AccountDtos.AccountResponse response = accountService.getAccountById(1L);

        assertNotNull(response);
        assertEquals("TB0000000001", response.accountNumber());
    }

    @Test
    void testGetAccountById_NotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountById(1L));
    }

    @Test
    void testGetAccountsByBank_Success() {
        when(bankRepository.existsById(1L)).thenReturn(true);
        List<Account> accounts = List.of(testAccount);
        when(accountRepository.findAllByBankId(1L)).thenReturn(accounts);
        when(accountMapper.toResponse(testAccount)).thenReturn(
                new AccountDtos.AccountResponse(1L, "TB0000000001", "John Doe", "john@example.com",
                        BigDecimal.valueOf(1000), AccountType.CHECKING, AccountStatus.ACTIVE, null, null, null)
        );

        List<AccountDtos.AccountResponse> responses = accountService.getAccountsByBank(1L);

        assertEquals(1, responses.size());
        verify(accountRepository, times(1)).findAllByBankId(1L);
    }

    @Test
    void testGetAccountsByBank_BankNotFound() {
        when(bankRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccountsByBank(1L));
    }

    @Test
    void testDeleteAccount_Success() {
        when(accountRepository.existsById(1L)).thenReturn(true);

        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAccount_NotFound() {
        when(accountRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccount(1L));
        verify(accountRepository, never()).deleteById(any());
    }
}
