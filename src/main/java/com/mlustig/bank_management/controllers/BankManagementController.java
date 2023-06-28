package com.mlustig.bank_management.controllers;

import com.mlustig.bank_management.dto.BankAccountDto;
import com.mlustig.bank_management.dto.BankAccountStatusDto;
import com.mlustig.bank_management.metrics.BankManagementMetricManager;
import com.mlustig.bank_management.requests.TransactionRequest;
import com.mlustig.bank_management.services.BankManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/bank/management")
public class BankManagementController {

    private final BankManagementService service;

    private final BankManagementMetricManager metricManager;

    @Autowired
    public BankManagementController(BankManagementService service, BankManagementMetricManager metricManager) {
        this.service = service;
        this.metricManager = metricManager;
    }

    @PutMapping("/{accountId}/activate")
    public ResponseEntity<BankAccountStatusDto> activateAccount(@PathVariable("accountId") String accountId) {
        return metricManager.getActivateAccountTimer().record(() -> {
            metricManager.getActivateAccountCounter().increment();
            Optional<BankAccountStatusDto> activatedAccount = service.activateAccount(accountId);
            return ResponseEntity.ok(activatedAccount.orElse(null));
        });
    }

    @PutMapping("/{accountId}/deactivate")
    public ResponseEntity<BankAccountStatusDto> deactivateAccount(@PathVariable("accountId") String accountId) {
        return metricManager.getDeactivateAccountTimer().record(() -> {
            metricManager.getDeactivateAccountCounter().increment();
            Optional<BankAccountStatusDto> deactivatedAccount = service.deactivateAccount(accountId);
            return ResponseEntity.ok(deactivatedAccount.orElse(null));
        });
    }

    @PostMapping("/deposit")
    public ResponseEntity<BankAccountDto> makeDeposit(@Valid @RequestBody TransactionRequest transaction) {
        return metricManager.getMakeDepositTimer().record(() -> {
            metricManager.getMakeDepositCounter().increment();
            Optional<BankAccountDto> updatedAccount =
                    service.makeDeposit(transaction.accountId(), transaction.amount());
            return ResponseEntity.ok(updatedAccount.orElse(null));
        });
    }

    @PostMapping("/withdraw")
    public ResponseEntity<BankAccountDto> makeWithdraw(@Valid @RequestBody TransactionRequest transaction) {
        return metricManager.getMakeWithdrawTimer().record(() -> {
            metricManager.getMakeWithdrawCounter().increment();
            Optional<BankAccountDto> updatedAccount =
                    service.makeWithdraw(transaction.accountId(), transaction.amount());
            return ResponseEntity.ok(updatedAccount.orElse(null));
        });
    }
}