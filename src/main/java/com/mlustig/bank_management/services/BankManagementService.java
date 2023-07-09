package com.mlustig.bank_management.services;

import com.mlustig.bank_management.dao.AccountBalance;
import com.mlustig.bank_management.dao.AccountProperties;
import com.mlustig.bank_management.dto.AccountBalanceDto;
import com.mlustig.bank_management.dto.AccountPropertiesDto;
import com.mlustig.bank_management.enums.TransactionType;
import com.mlustig.bank_management.exceptions.EmailValidationException;
import com.mlustig.bank_management.exceptions.InactiveAccountException;
import com.mlustig.bank_management.exceptions.InsufficientFundsException;
import com.mlustig.bank_management.facades.DataFacade;
import com.mlustig.bank_management.mappers.AccountBalanceMapper;
import com.mlustig.bank_management.mappers.AccountPropertiesMapper;
import com.mlustig.bank_management.validators.EmailValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankManagementService {

    private final DataFacade dataFacade;
    private final AccountPropertiesMapper propertiesMapper;
    private final AccountBalanceMapper balanceMapper;

    public Optional<AccountPropertiesDto> updateCreditLimit(String userName, double creditLimit) {
        log.info("BankManagementService.updateCreditLimit(userName) - update bank account credit limit. userName: {}, creditLimit: {}", userName, creditLimit);
        validateUserName(userName);
        if (dataFacade.updateAccountPropertiesCreditLimit(userName, BigDecimal.valueOf(creditLimit)) > 0) {
            return dataFacade.findAccountPropertiesByAccountInfoUserName(userName).map(propertiesMapper::toDto);
        } else {
            return Optional.empty();
        }
    }

    public Optional<AccountPropertiesDto> activateAccount(String userName) {
        log.info("BankManagementService.activateAccount(userName) - make a bank account active. userName: {}", userName);
        validateUserName(userName);
        Optional<AccountProperties> original = dataFacade.findAccountPropertiesByAccountInfoUserName(userName);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        } else if (original.get().isActive()) {
            return Optional.of(propertiesMapper.toDto(original.get()));
        } else {
            if (dataFacade.updateAccountPropertiesActiveStatus(userName, true) > 0) {
                return Optional.of(new AccountPropertiesDto(original.get().getCreditLimit(), true));
            } else {
                return Optional.empty();
            }
        }
    }

    public Optional<AccountPropertiesDto> deactivateAccount(String userName) {
        log.info("BankManagementService.deactivateAccount(userName) - make a bank account inactive. userName: {}", userName);
        validateUserName(userName);
        Optional<AccountProperties> original = dataFacade.findAccountPropertiesByAccountInfoUserName(userName);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        } else if (!original.get().isActive()) {
            return Optional.of(propertiesMapper.toDto(original.get()));
        } else {
            if (dataFacade.updateAccountPropertiesActiveStatus(userName, false) > 0) {
                return Optional.of(new AccountPropertiesDto(original.get().getCreditLimit(), false));
            } else {
                return Optional.empty();
            }
        }
    }

    public Optional<AccountBalanceDto> makeDeposit(String userName, double amount) {
        log.info("BankManagementService.makeDeposit(userName,amount) - make a deposit to bank account. userName: {}, amount: {}", userName, amount);
        validateUserName(userName);

        Optional<AccountProperties> properties = dataFacade.findAccountPropertiesByAccountInfoUserName(userName);
        validateAccountExists(properties);
        validateAccountActive(properties);
        Optional<AccountBalance> balanceOptional = dataFacade.findAccountBalanceByAccountInfoUserName(userName);

        if (balanceOptional.isPresent()) {
            BigDecimal depositAmount = BigDecimal.valueOf(amount);
            dataFacade.saveTransaction(userName, depositAmount, TransactionType.DEPOSIT);
            return balanceOptional.map(balance -> {
                BigDecimal updatedBalance = balance.getBalance().add(depositAmount);
                dataFacade.updateAccountBalance(userName, updatedBalance);
                return new AccountBalanceDto(updatedBalance, LocalDateTime.now());
            });
        } else {
            return Optional.empty();
        }
    }

    public Optional<AccountBalanceDto> makeWithdraw(String userName, double amount) {
        log.info("BankManagementService.makeWithdraw(userName, amount) - make a withdraw for bank account. userName: {}, amount: {}", userName, amount);
        validateUserName(userName);

        Optional<AccountProperties> optionalAccountProperties =
                dataFacade.findAccountPropertiesByAccountInfoUserName(userName);
        validateAccountExists(optionalAccountProperties);
        validateAccountActive(optionalAccountProperties);
        Optional<AccountBalance> optionalAccountBalance =
                dataFacade.findAccountBalanceByAccountInfoUserName(userName);
        validateSufficientFunds(optionalAccountBalance, optionalAccountProperties, amount);

        if (optionalAccountBalance.isPresent()) {
            BigDecimal depositAmount = BigDecimal.valueOf(amount);
            dataFacade.saveTransaction(userName, depositAmount, TransactionType.WITHDRAW);
            return optionalAccountBalance.map(balance -> {
                BigDecimal updatedBalance = balance.getBalance().subtract(depositAmount);
                if (dataFacade.updateAccountBalance(userName, updatedBalance) > 0) {
                    return new AccountBalanceDto(updatedBalance, LocalDateTime.now());
                } else {
                    return null;
                }
            });
        } else {
            return Optional.empty();
        }
    }

    public Optional<AccountBalanceDto> getBalance(String userName) {
        log.info("BankManagementService.getBalance(userName) - get balance. userName: {}", userName);
        validateUserName(userName);
        Optional<AccountBalance> balanceOptional = dataFacade.findAccountBalanceByAccountInfoUserName(userName);
        return balanceOptional.map(balanceMapper::toDto);
    }

    private void validateUserName(String userName) {
        if (EmailValidator.isValid(userName)) {
            throw new EmailValidationException();
        }
    }

    private void validateAccountExists(Optional<AccountProperties> accountProperties) {
        if (accountProperties.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
    }

    private void validateAccountActive(Optional<AccountProperties> accountProperties) {
        if (!accountProperties.get().isActive()) {
            throw new InactiveAccountException();
        }
    }

    private void validateSufficientFunds(Optional<AccountBalance> accountBalance,
                                         Optional<AccountProperties> accountProperties, double amount) {
        double balance = accountBalance.get().getBalance().doubleValue();
        double minimumBalance = accountProperties.get().getCreditLimit().doubleValue();
        if (balance - amount < minimumBalance) {
            throw new InsufficientFundsException();
        }
    }
}