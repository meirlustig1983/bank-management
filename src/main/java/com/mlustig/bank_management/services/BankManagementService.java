package com.mlustig.bank_management.services;

import com.mlustig.bank_management.dao.BankAccount;
import com.mlustig.bank_management.dao.BankAccountStatus;
import com.mlustig.bank_management.dto.BankAccountDto;
import com.mlustig.bank_management.dto.BankAccountStatusDto;
import com.mlustig.bank_management.enums.BankAccountFields;
import com.mlustig.bank_management.enums.TransactionType;
import com.mlustig.bank_management.exceptions.EmailValidationException;
import com.mlustig.bank_management.exceptions.InactiveAccountException;
import com.mlustig.bank_management.exceptions.InsufficientFundsException;
import com.mlustig.bank_management.facades.DataFacade;
import com.mlustig.bank_management.mappers.BankAccountMapper;
import com.mlustig.bank_management.mappers.BankAccountStatusMapper;
import com.mlustig.bank_management.validators.EmailValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BankManagementService {

    private final DataFacade dataFacade;
    private final BankAccountStatusMapper statusMapper;

    public Optional<BankAccountStatusDto> activateAccount(String accountId) {
        log.info("BankManagementService.activateAccount(accountId) - make a bank account active. accountId: {}", accountId);
        validateAccountId(accountId);

        Optional<BankAccountStatus> original = dataFacade.findBankAccountStatusByAccountId(accountId);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        } else if (original.get().isActive()) {
            return Optional.of(statusMapper.toDto(original.get()));
        } else {
            return dataFacade.updateBankAccount(accountId, List.of(Pair.of(BankAccountFields.ACTIVE, "true"))).map(mapper::toDto);
        }
    }

    public Optional<BankAccountStatusDto> deactivateAccount(String accountId) {
        log.info("BankManagementService.deactivateAccount(accountId) - make a bank account inactive. accountId: {}", accountId);
        validateAccountId(accountId);

        Optional<BankAccountStatus> original = dataFacade.findBankAccountStatusByAccountId(accountId);
        if (original.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        } else if (!original.get().isActive()) {
            return Optional.of(statusMapper.toDto(original.get()));
        } else {
            return dataFacade.updateBankAccount(accountId, List.of(Pair.of(BankAccountFields.ACTIVE, "false"))).map(mapper::toDto);
        }
    }

    public Optional<BankAccountDto> makeDeposit(String accountId, double amount) {
        log.info("BankManagementService.makeDeposit(accountId,amount) - make a deposit to bank account. accountId: {}, amount: {}", accountId, amount);
        validateAccountId(accountId);

        Optional<BankAccount> original = dataFacade.findBankAccountByAccountId(accountId);
        validateAccountExists(original);
        validateAccountActive(original);

        BigDecimal depositAmount = BigDecimal.valueOf(amount);
        dataFacade.saveTransaction(original.get().getId(), depositAmount, TransactionType.DEPOSIT);

        return original.map(account -> {
            BigDecimal updatedBalance = account.getBalance().add(depositAmount);
            return updateAccountBalance(accountId, updatedBalance);
        });
    }

    public Optional<BankAccountDto> makeWithdraw(String accountId, double amount) {
        log.info("BankManagementService.makeWithdraw(id, amount) - make a withdraw for bank account. accountId: {}, amount: {}", accountId, amount);
        validateAccountId(accountId);

        Optional<BankAccount> original = dataFacade.findBankAccountByAccountId(accountId);
        validateAccountExists(original);
        validateAccountActive(original);
        validateSufficientFunds(original, amount);

        BigDecimal withdrawalAmount = BigDecimal.valueOf(amount);
        dataFacade.saveTransaction(original.get().getId(), withdrawalAmount, TransactionType.WITHDRAW);

        return original.map(account -> {
            BigDecimal updatedBalance = account.getBalance().subtract(withdrawalAmount);
            return updateAccountBalance(accountId, updatedBalance);
        });
    }

    private void validateAccountId(String accountId) {
        if (EmailValidator.isValid(accountId)) {
            throw new EmailValidationException();
        }
    }

    private void validateAccountExists(Optional<BankAccount> bankAccount) {
        if (bankAccount.isEmpty()) {
            throw new EntityNotFoundException("Invalid bank account");
        }
    }

    private void validateAccountActive(Optional<BankAccount> bankAccount) {
        if (!bankAccount.get().isActive()) {
            throw new InactiveAccountException();
        }
    }

    private void validateSufficientFunds(Optional<BankAccount> bankAccount, double amount) {
        double balance = bankAccount.get().getBalance().doubleValue();
        double minimumBalance = bankAccount.get().getMinimumBalance().doubleValue();
        if (balance - amount < minimumBalance) {
            throw new InsufficientFundsException();
        }
    }

    private BankAccountDto updateAccountBalance(String accountId, BigDecimal newBalance) {
        return dataFacade.updateBankAccount(accountId, List.of(Pair.of(BankAccountFields.BALANCE, newBalance.toString())))
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Failed to update bank account balance"));
    }
}