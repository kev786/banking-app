package com.banking.app.services;

import com.banking.app.dtos.BankDtos;

import java.util.List;

public interface BankService {

    BankDtos.BankResponse createBank(BankDtos.CreateBankRequest request);

    List<BankDtos.BankResponse> getAllBanks();

    BankDtos.BankResponse getBankById(Long id);
}
