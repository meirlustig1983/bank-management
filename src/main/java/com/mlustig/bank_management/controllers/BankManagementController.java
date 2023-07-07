package com.mlustig.bank_management.controllers;

import com.mlustig.bank_management.dto.AccountBalanceDto;
import com.mlustig.bank_management.dto.AccountPropertiesDto;
import com.mlustig.bank_management.metrics.BankManagementMetricManager;
import com.mlustig.bank_management.requests.CreditLimitRequest;
import com.mlustig.bank_management.requests.TransactionRequest;
import com.mlustig.bank_management.services.BankManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bank/management")
public class BankManagementController {

    private final BankManagementService service;

    private final BankManagementMetricManager metricManager;

    @PostMapping("/credit-limit")
    public ResponseEntity<AccountPropertiesDto> updateCredit(@Valid @RequestBody CreditLimitRequest creditLimitRequest) {
        return metricManager.getUpdateCreditTimer().record(() -> {
            metricManager.getUpdateCreditCounter().increment();
            Optional<AccountPropertiesDto> activatedAccount = service.updateCreditLimit(creditLimitRequest.userName(), creditLimitRequest.amount());
            return ResponseEntity.ok(activatedAccount.orElse(null));
        });
    }

    @PutMapping("/{userName}/activate")
    public ResponseEntity<AccountPropertiesDto> activateAccount(@PathVariable("userName") String userName) {
        return metricManager.getActivateAccountTimer().record(() -> {
            metricManager.getActivateAccountCounter().increment();
            Optional<AccountPropertiesDto> activatedAccount = service.activateAccount(userName);
            return ResponseEntity.ok(activatedAccount.orElse(null));
        });
    }

    @PutMapping("/{userName}/deactivate")
    public ResponseEntity<AccountPropertiesDto> deactivateAccount(@PathVariable("userName") String userName) {
        return metricManager.getDeactivateAccountTimer().record(() -> {
            metricManager.getDeactivateAccountCounter().increment();
            Optional<AccountPropertiesDto> deactivatedAccount = service.deactivateAccount(userName);
            return ResponseEntity.ok(deactivatedAccount.orElse(null));
        });
    }

    @PostMapping("/deposit")
    public ResponseEntity<AccountBalanceDto> makeDeposit(@Valid @RequestBody TransactionRequest transaction) {
        return metricManager.getMakeDepositTimer().record(() -> {
            metricManager.getMakeDepositCounter().increment();
            Optional<AccountBalanceDto> updatedAccount =
                    service.makeDeposit(transaction.userName(), transaction.amount());
            return ResponseEntity.ok(updatedAccount.orElse(null));
        });
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountBalanceDto> makeWithdraw(@Valid @RequestBody TransactionRequest transaction) {
        return metricManager.getMakeWithdrawTimer().record(() -> {
            metricManager.getMakeWithdrawCounter().increment();
            Optional<AccountBalanceDto> updatedAccount =
                    service.makeWithdraw(transaction.userName(), transaction.amount());
            return ResponseEntity.ok(updatedAccount.orElse(null));
        });
    }

    @PostMapping("{userName}/balance")
    public ResponseEntity<AccountBalanceDto> getBalance(@PathVariable String userName) {
        return metricManager.getGetBalanceTimer().record(() -> {
            metricManager.getGetBalanceCounter().increment();
            return ResponseEntity.ok(service.getBalance(userName).orElse(null));
        });
    }
}