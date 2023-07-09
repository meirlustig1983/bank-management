package com.mlustig.bank_management.facades;

import com.mlustig.bank_management.dao.AccountBalance;
import com.mlustig.bank_management.dao.AccountInfo;
import com.mlustig.bank_management.dao.AccountProperties;
import com.mlustig.bank_management.dao.Transaction;
import com.mlustig.bank_management.enums.TransactionType;
import com.mlustig.bank_management.repositories.AccountBalanceRepository;
import com.mlustig.bank_management.repositories.AccountInfoRepository;
import com.mlustig.bank_management.repositories.AccountPropertiesRepository;
import com.mlustig.bank_management.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DataFacade {

    private final AccountBalanceRepository accountBalanceRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final AccountPropertiesRepository accountPropertiesRepository;
    private final TransactionRepository transactionRepository;

    public Optional<AccountBalance> findAccountBalanceByAccountInfoUserName(String userName) {
        return accountBalanceRepository.findByAccountInfo_UserName(userName);
    }

    public void createAccountBalance(String userName, BigDecimal balance) {
        Optional<AccountInfo> accountInfo = accountInfoRepository.findByUserName(userName);
        if (accountInfo.isPresent()) {
            AccountBalance accountBalance = AccountBalance.builder()
                    .balance(balance)
                    .accountInfo(accountInfo.get())
                    .updatedAt(LocalDateTime.now())
                    .build();
            accountBalanceRepository.save(accountBalance);
        }
    }

    public int updateAccountBalance(String userName, BigDecimal balance) {
        return accountBalanceRepository.updateBalanceByUserName(userName, balance);
    }

    public Optional<AccountInfo> saveAccountInfo(AccountInfo accountInfo) {
        return Optional.of(accountInfoRepository.save(accountInfo));
    }

    public Optional<AccountInfo> findAccountInfoByUserName(String userName) {
        return accountInfoRepository.findByUserName(userName);
    }

    public Optional<AccountInfo> findAccountInfoByEmail(String email) {
        return accountInfoRepository.findByEmail(email);
    }

    public Optional<AccountInfo> findAccountInfoByPhoneNumber(String phoneNumber) {
        return accountInfoRepository.findByPhoneNumber(phoneNumber);
    }

    public void deleteAccountInfoByUserName(String userName) {
        accountInfoRepository.deleteByUserName(userName);
    }

    public void saveAccountProperties(String userName, boolean active, BigDecimal creditLimit) {
        Optional<AccountInfo> accountInfo = accountInfoRepository.findByUserName(userName);
        if (accountInfo.isPresent()) {
            AccountProperties accountProperties = AccountProperties.builder()
                    .accountInfo(accountInfo.get())
                    .active(active)
                    .creditLimit(creditLimit)
                    .updatedAt(LocalDateTime.now())
                    .build();
            accountPropertiesRepository.save(accountProperties);
        }
    }

    public void saveAccountBalance(String userName, BigDecimal balance) {
        Optional<AccountInfo> accountInfo = accountInfoRepository.findByUserName(userName);
        if (accountInfo.isPresent()) {
            AccountBalance accountBalance = AccountBalance.builder()
                    .accountInfo(accountInfo.get())
                    .balance(balance)
                    .updatedAt(LocalDateTime.now())
                    .build();
            accountBalanceRepository.save(accountBalance);
        }
    }

    public Optional<AccountProperties> findAccountPropertiesByAccountInfoUserName(String userName) {
        return accountPropertiesRepository.findByAccountInfo_UserName(userName);
    }

    public void saveTransaction(String userName, BigDecimal amount, TransactionType type) {
        Optional<AccountInfo> accountInfo = accountInfoRepository.findByUserName(userName);
        if (accountInfo.isPresent()) {
            Transaction transaction = Transaction.builder()
                    .accountInfo(accountInfo.get())
                    .amount(amount)
                    .type(type)
                    .build();
            transactionRepository.save(transaction);
        }
    }

    public int updateAccountPropertiesActiveStatus(String userName, boolean active) {
        return accountPropertiesRepository.updateActiveStatusByUserName(userName, active);
    }

    public int updateAccountPropertiesCreditLimit(String userName, BigDecimal creditLimit) {
        return accountPropertiesRepository.updateCreditLimitByUserName(userName, creditLimit);
    }
}