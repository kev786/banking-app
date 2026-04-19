package com.banking.app.repositories;

import com.banking.app.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    boolean existsByCode(String code);

    boolean existsByName(String name);
}
