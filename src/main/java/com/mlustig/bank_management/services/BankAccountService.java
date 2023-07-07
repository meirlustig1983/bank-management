package com.mlustig.bank_management.services;

import com.mlustig.bank_management.dao.AccountInfo;
import com.mlustig.bank_management.dto.AccountInfoDto;
import com.mlustig.bank_management.exceptions.EmailValidationException;
import com.mlustig.bank_management.facades.DataFacade;
import com.mlustig.bank_management.mappers.AccountInfoMapper;
import com.mlustig.bank_management.validators.EmailValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class BankAccountService {

    private final DataFacade dataFacade;
    private final AccountInfoMapper mapper;

    public BankAccountService(DataFacade dataFacade, AccountInfoMapper mapper) {
        this.dataFacade = dataFacade;
        this.mapper = mapper;
    }

    public Optional<AccountInfoDto> getAccountInfoByUserName(String userName) {
        log.info("BankAccountService.getAccountInfoByUserName(userName) - get info about bank account. userName: {}", userName);
        validateUserName(userName);
        Optional<AccountInfo> bankAccount = dataFacade.findAccountInfoByUserName(userName);
        if (bankAccount.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return bankAccount.map(mapper::toDto);
    }

    public Optional<AccountInfoDto> getAccountInfoByPhoneNumber(String phoneNumber) {
        log.info("BankAccountService.getAccountInfoByPhoneNumber(userName) - get info about bank account. phoneNumber: {}", phoneNumber);
        Optional<AccountInfo> bankAccount = dataFacade.findAccountInfoByPhoneNumber(phoneNumber);
        if (bankAccount.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return bankAccount.map(mapper::toDto);
    }

    public Optional<AccountInfoDto> getAccountInfoByEmail(String email) {
        log.info("BankAccountService.getAccountInfoByEmail(email) - get info about bank account. email: {}", email);
        validateUserName(email);
        Optional<AccountInfo> bankAccount = dataFacade.findAccountInfoByEmail(email);
        if (bankAccount.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return bankAccount.map(mapper::toDto);
    }

    public Optional<AccountInfoDto> createAccount(AccountInfoDto accountInfoDto) {
        log.info("BankAccountService.createAccount(accountInfoDto) - create bank account");
        Optional<AccountInfo> bankAccount = dataFacade.saveAccountInfo(mapper.toDao(accountInfoDto));
        if (bankAccount.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
        return bankAccount.map(mapper::toDto);
    }

    public void deleteBankAccountByUserName(String userName) {
        log.info("BankAccountService.deleteBankAccountByUserName(userName) - delete bank account. userName: {}", userName);
        validateUserName(userName);
        dataFacade.deleteAccountInfoByUserName(userName);
    }

    private void validateUserName(String userName) {
        if (EmailValidator.isValid(userName)) {
            throw new EmailValidationException();
        }
    }
}