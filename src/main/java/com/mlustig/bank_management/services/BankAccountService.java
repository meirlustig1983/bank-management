package com.mlustig.bank_management.services;

import com.mlustig.bank_management.dao.BankAccount;
import com.mlustig.bank_management.dto.BankAccountDto;
import com.mlustig.bank_management.exceptions.EmailValidationException;
import com.mlustig.bank_management.facades.DataFacade;
import com.mlustig.bank_management.mappers.BankAccountMapper;
import com.mlustig.bank_management.validators.EmailValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BankAccountService {

    private final DataFacade dataFacade;
    private final BankAccountMapper mapper;

    public BankAccountService(DataFacade dataFacade, BankAccountMapper mapper) {
        this.dataFacade = dataFacade;
        this.mapper = mapper;
    }

    public Optional<BankAccountDto> getAccountInfo(String accountId) {
        log.info("BankAccountService.getAccountInfo(accountId) - get info about bank account. accountId: {}", accountId);
        validateAccountId(accountId);
        Optional<BankAccount> bankAccount = dataFacade.findBankAccountByAccountId(accountId);
        if (bankAccount.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return bankAccount.map(mapper::toDto);
    }

    public Optional<BankAccountDto> createAccount(BankAccountDto bankAccountDto) {
        log.info("BankAccountService.createAccount(bankAccount) - create bank account");
        return dataFacade.saveBankAccount(mapper.toDao(bankAccountDto)).map(mapper::toDto);
    }

    public void deleteBankAccountByAccountId(String accountId) {
        log.info("BankAccountService.deleteBankAccountByAccountId(accountId) - delete bank account. accountId: {}", accountId);
        validateAccountId(accountId);
        dataFacade.deleteBankAccountByAccountId(accountId);
    }

    private void validateAccountId(String accountId) {
        if (EmailValidator.isValid(accountId)) {
            throw new EmailValidationException();
        }
    }
}