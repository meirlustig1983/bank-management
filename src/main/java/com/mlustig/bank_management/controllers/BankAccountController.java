package com.mlustig.bank_management.controllers;

import com.mlustig.bank_management.dto.AccountInfoDto;
import com.mlustig.bank_management.metrics.BankAccountMetricManager;
import com.mlustig.bank_management.services.BankAccountService;
import com.mlustig.bank_management.utils.ControllerHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bank/account")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    private final BankAccountMetricManager bankAccountMetricManager;

    @GetMapping("/{userName}")
    public ResponseEntity<AccountInfoDto> getAccountInfoByUserName(@PathVariable("userName") String userName) {
        return bankAccountMetricManager.getGetAccountInfoByUserNameTimer().record(() -> {
            bankAccountMetricManager.getGetAccountInfoByUserNameCounter().increment();
            Optional<AccountInfoDto> accountInfo = bankAccountService.getAccountInfoByUserName(userName);
            return ResponseEntity.ok(accountInfo.orElse(null));
        });
    }

    @GetMapping
    public ResponseEntity<AccountInfoDto> getAccountInfoByEmail(@RequestParam("email") String email) {
        return bankAccountMetricManager.getGetAccountInfoByEmailTimer().record(() -> {
            bankAccountMetricManager.getGetAccountInfoByEmailCounter().increment();
            Optional<AccountInfoDto> accountInfo = bankAccountService.getAccountInfoByEmail(email);
            return ResponseEntity.ok(accountInfo.orElse(null));
        });
    }

    @GetMapping
    public ResponseEntity<AccountInfoDto> getAccountInfoByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return bankAccountMetricManager.getGetAccountInfoByPhoneNumberTimer().record(() -> {
            bankAccountMetricManager.getGetAccountInfoByPhoneNumberCounter().increment();
            Optional<AccountInfoDto> accountInfo = bankAccountService.getAccountInfoByPhoneNumber(phoneNumber);
            return ResponseEntity.ok(accountInfo.orElse(null));
        });
    }

    @PostMapping
    public ResponseEntity<AccountInfoDto> createAccount(@Valid @RequestBody AccountInfoDto bankAccountDto) {
        return bankAccountMetricManager.getCreateAccountTimer().record(() -> {
            bankAccountMetricManager.getCreateAccountCounter().increment();
            return bankAccountService.createAccount(bankAccountDto)
                    .map(dto -> ResponseEntity.created(ControllerHelper.getLocation()).body(dto))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid bank account data"));
        });
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable("userName") String userName) {
        return bankAccountMetricManager.getDeleteBankAccountTimer().record(() -> {
            bankAccountMetricManager.getDeleteBankAccountCounter().increment();
            bankAccountService.deleteBankAccountByUserName(userName);
            return ResponseEntity.noContent().build();
        });
    }
}