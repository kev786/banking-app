package com.banking.app.mappers;

import com.banking.app.dtos.AccountDtos;
import com.banking.app.entities.Account;
import com.banking.app.entities.Bank;
import com.banking.app.enums.AccountStatus;
import com.banking.app.enums.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountMapperTest {

    @Autowired
    private AccountMapper accountMapper;

    private Bank testBank;
    private AccountDtos.CreateAccountRequest createRequest;

    @BeforeEach
    void setUp() {
        testBank = Bank.builder()
                .id(1L)
                .name("Test Bank")
                .code("TB")
                .build();

        createRequest = new AccountDtos.CreateAccountRequest(
                "John Doe",
                "john@example.com",
                BigDecimal.valueOf(1000),
                AccountType.CHECKING,
                1L
        );
    }

    @Test
    void testToEntity() {
        Account account = accountMapper.toEntity(createRequest, testBank);

        assertNotNull(account);
        assertEquals("John Doe", account.getOwnerName());
        assertEquals("john@example.com", account.getOwnerEmail());
        assertEquals(AccountType.CHECKING, account.getAccountType());
        assertEquals(BigDecimal.valueOf(1000), account.getBalance());
        assertEquals(testBank, account.getBank());
    }

    @Test
    void testToResponse() {
        Account account = Account.builder()
                .id(1L)
                .accountNumber("TB0000000001")
                .ownerName("John Doe")
                .ownerEmail("john@example.com")
                .balance(BigDecimal.valueOf(1000))
                .accountType(AccountType.CHECKING)
                .status(AccountStatus.ACTIVE)
                .bank(testBank)
                .build();

        AccountDtos.AccountResponse response = accountMapper.toResponse(account);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("TB0000000001", response.accountNumber());
        assertEquals("John Doe", response.ownerName());
        assertEquals("john@example.com", response.ownerEmail());
        assertEquals(BigDecimal.valueOf(1000), response.balance());
        assertEquals(AccountType.CHECKING, response.accountType());
        assertEquals(AccountStatus.ACTIVE, response.status());
    }
}
