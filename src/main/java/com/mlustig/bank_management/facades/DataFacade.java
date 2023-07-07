package com.mlustig.bank_management.facades;

import com.mlustig.bank_management.dao.AccountBalance;
import com.mlustig.bank_management.dao.AccountInfo;
import com.mlustig.bank_management.dao.AccountProperties;
import com.mlustig.bank_management.dao.Transaction;
import com.mlustig.bank_management.enums.AccountInfoFields;
import com.mlustig.bank_management.enums.AccountPropertiesFields;
import com.mlustig.bank_management.enums.TransactionType;
import com.mlustig.bank_management.repositories.AccountBalanceRepository;
import com.mlustig.bank_management.repositories.AccountInfoRepository;
import com.mlustig.bank_management.repositories.AccountPropertiesRepository;
import com.mlustig.bank_management.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

    public void updateAccountBalance(String userName, BigDecimal balance) {
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

    public Optional<AccountInfo> updateAccountInfo(String userName, List<Pair<AccountInfoFields, String>> data) {
        Optional<AccountInfo> original = accountInfoRepository.findByUserName(userName);
        return original.map(account -> {
            AccountInfo.AccountInfoBuilder builder = AccountInfo.builder()
                    .accountInfoId(account.getAccountInfoId())
                    .userName(account.getUserName())
                    .firstName(account.getFirstName())
                    .lastName(account.getLastName())
                    .email(account.getEmail())
                    .phoneNumber(account.getPhoneNumber())
                    .updatedAt(LocalDateTime.now())
                    .createdAt(account.getCreatedAt());

            data.forEach(pair -> {
                AccountInfoFields field = pair.getFirst();
                String value = pair.getSecond();

                switch (field) {
                    case USER_NAME -> builder.userName(value);
                    case FIRST_NAME -> builder.firstName(value);
                    case LAST_NAME -> builder.lastName(value);
                    case EMAIL -> builder.email(value);
                    case PHONE_NUMBER -> builder.phoneNumber(value);
                    default -> throw new IllegalArgumentException("You are unauthorized to update this field.");
                }
            });

            AccountInfo updated = builder.build();
            return accountInfoRepository.save(updated);
        });
    }

    public Optional<AccountProperties> updateAccountProperties(String userName, List<Pair<AccountPropertiesFields, String>> data) {
        Optional<AccountProperties> original = accountPropertiesRepository.findByAccountInfo_UserName(userName);
        return original.map(properties -> {
            AccountProperties.AccountPropertiesBuilder builder = AccountProperties.builder()
                    .accountPropertiesId(properties.getAccountPropertiesId())
                    .active(properties.isActive())
                    .creditLimit(properties.getCreditLimit())
                    .updatedAt(LocalDateTime.now())
                    .createdAt(properties.getCreatedAt());

            data.forEach(pair -> {
                AccountPropertiesFields field = pair.getFirst();
                String value = pair.getSecond();

                switch (field) {
                    case CREDIT_LIMIT -> builder.creditLimit(new BigDecimal(value));
                    case ACTIVE -> builder.active(Boolean.parseBoolean(value));
                    default -> throw new IllegalArgumentException("You are unauthorized to update this field.");
                }
            });
            AccountProperties updated = builder.build();
            return accountPropertiesRepository.save(updated);
        });
    }
}