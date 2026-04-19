package com.banking.app.repositories;

import com.banking.app.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM Account a JOIN FETCH a.bank WHERE a.bank.id = :bankId")
    List<Account> findAllByBankId(@Param("bankId") Long bankId);

    @Query("SELECT a FROM Account a JOIN FETCH a.bank")
    List<Account> findAllWithBank();
}
