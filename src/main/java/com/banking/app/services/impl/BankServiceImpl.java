package com.banking.app.services.impl;

import com.banking.app.dtos.BankDtos;
import com.banking.app.exceptions.DuplicateResourceException;
import com.banking.app.exceptions.ResourceNotFoundException;
import com.banking.app.mappers.BankMapper;
import com.banking.app.repositories.BankRepository;
import com.banking.app.services.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    @Override
    @Transactional
    public BankDtos.BankResponse createBank(BankDtos.CreateBankRequest request) {
        if (bankRepository.existsByCode(request.code())) {
            throw DuplicateResourceException.bankCode(request.code());
        }
        var bank = bankMapper.toEntity(request);
        var saved = bankRepository.save(bank);
        return bankMapper.toResponse(saved);
    }

    @Override
    public List<BankDtos.BankResponse> getAllBanks() {
        return bankRepository.findAll()
                .stream()
                .map(bankMapper::toResponse)
                .toList();
    }

    @Override
    public BankDtos.BankResponse getBankById(Long id) {
        return bankRepository.findById(id)
                .map(bankMapper::toResponse)
                .orElseThrow(() -> ResourceNotFoundException.bank(id));
    }
}
