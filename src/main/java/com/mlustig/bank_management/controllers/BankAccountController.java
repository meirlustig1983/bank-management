package com.mlustig.bank_management.controllers;

import com.mlustig.bank_management.dto.BankAccountDto;
import com.mlustig.bank_management.metrics.BankAccountMetricManager;
import com.mlustig.bank_management.services.BankAccountService;
import com.mlustig.bank_management.utils.ControllerHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bank/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    private final BankAccountMetricManager bankAccountMeterRegistry;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService, BankAccountMetricManager bankAccountMeterRegistry) {
        this.bankAccountService = bankAccountService;
        this.bankAccountMeterRegistry = bankAccountMeterRegistry;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<BankAccountDto> getAccountInfo(@PathVariable("accountId") String accountId) {
        return bankAccountMeterRegistry.getGetAccountInfoTimer().record(() -> {
            bankAccountMeterRegistry.getGetAccountInfoCounter().increment();
            Optional<BankAccountDto> accountInfo = bankAccountService.getAccountInfo(accountId);
            return ResponseEntity.ok(accountInfo.orElse(null));
        });
    }

    @PostMapping
    public ResponseEntity<BankAccountDto> createAccount(@Valid @RequestBody BankAccountDto bankAccountDto) {
        return bankAccountMeterRegistry.getCreateAccountTimer().record(() -> {
            bankAccountMeterRegistry.getCreateAccountCounter().increment();
            return bankAccountService.createAccount(bankAccountDto)
                    .map(dto -> ResponseEntity.created(ControllerHelper.getLocation()).body(dto))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid bank account data"));
        });
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable("accountId") String accountId) {
        return bankAccountMeterRegistry.getDeleteBankAccountTimer().record(() -> {
            bankAccountMeterRegistry.getDeleteBankAccountCounter().increment();
            bankAccountService.deleteBankAccountByAccountId(accountId);
            return ResponseEntity.noContent().build();
        });
    }
}