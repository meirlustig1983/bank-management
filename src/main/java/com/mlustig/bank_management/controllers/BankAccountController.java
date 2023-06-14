package com.mlustig.bank_management.controllers;

import com.mlustig.bank_management.dto.BankAccountDto;
import com.mlustig.bank_management.metrics.BankAccountMeterRegistry;
import com.mlustig.bank_management.requests.TransactionRequest;
import com.mlustig.bank_management.services.BankAccountService;
import com.mlustig.bank_management.utils.ControllerHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bank-accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    private final BankAccountMeterRegistry meterRegistry;

    @GetMapping("/{accountId}")
    public ResponseEntity<BankAccountDto> getAccountInfo(@PathVariable("accountId") String accountId) {
        return meterRegistry.getGetAccountInfoTimer().record(() -> {
            meterRegistry.getGetAccountInfoCounter().increment();
            Optional<BankAccountDto> accountInfo = bankAccountService.getAccountInfo(accountId);
            return ResponseEntity.ok(accountInfo.get());
        });
    }

    @PostMapping
    public ResponseEntity<BankAccountDto> createAccount(@Valid @RequestBody BankAccountDto bankAccountDto) {
        return meterRegistry.getCreateAccountTimer().record(() -> {
            meterRegistry.getCreateAccountCounter().increment();
            return bankAccountService.createAccount(bankAccountDto)
                    .map(dto -> ResponseEntity.created(ControllerHelper.getLocation()).body(dto))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid bank account data"));
        });
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable("accountId") String accountId) {
        return meterRegistry.getDeleteBankAccountTimer().record(() -> {
            meterRegistry.getDeleteBankAccountCounter().increment();
            bankAccountService.deleteBankAccountByAccountId(accountId);
            return ResponseEntity.noContent().build();
        });
    }

    @PutMapping("/{accountId}/activate")
    public ResponseEntity<BankAccountDto> activateAccount(@PathVariable("accountId") String accountId) {
        return meterRegistry.getActivateAccountTimer().record(() -> {
            meterRegistry.getActivateAccountCounter().increment();
            Optional<BankAccountDto> activatedAccount = bankAccountService.activateAccount(accountId);
            return ResponseEntity.ok(activatedAccount.get());
        });
    }

    @PutMapping("/{accountId}/deactivate")
    public ResponseEntity<BankAccountDto> deactivateAccount(@PathVariable("accountId") String accountId) {
        return meterRegistry.getDeactivateAccountTimer().record(() -> {
            meterRegistry.getDeactivateAccountCounter().increment();
            Optional<BankAccountDto> deactivatedAccount = bankAccountService.deactivateAccount(accountId);
            return ResponseEntity.ok(deactivatedAccount.get());
        });
    }

    @PostMapping("/deposit")
    public ResponseEntity<BankAccountDto> makeDeposit(@Valid @RequestBody TransactionRequest transaction) {
        return meterRegistry.getMakeDepositTimer().record(() -> {
            meterRegistry.getMakeDepositCounter().increment();
            Optional<BankAccountDto> updatedAccount =
                    bankAccountService.makeDeposit(transaction.accountId(), transaction.amount());
            return ResponseEntity.ok(updatedAccount.get());
        });
    }

    @PostMapping("/withdraw")
    public ResponseEntity<BankAccountDto> makeWithdraw(@Valid @RequestBody TransactionRequest transaction) {
        return meterRegistry.getMakeWithdrawTimer().record(() -> {
            meterRegistry.getMakeWithdrawCounter().increment();
            Optional<BankAccountDto> updatedAccount =
                    bankAccountService.makeWithdraw(transaction.accountId(), transaction.amount());
            return ResponseEntity.ok(updatedAccount.get());
        });
    }
}