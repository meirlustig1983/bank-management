package com.ml.bank_management.services;

import com.ml.bank_management.dto.BankAccountDto;
import com.ml.bank_management.exceptions.InsufficientFundsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Transactional
@SpringBootTest
@Sql(scripts = "/data/recreate-datasets-1.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/data/clean-database.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BankAccountServiceIT {

    @Autowired
    private BankAccountService service;

    @Test
    @DisplayName("Test get info about bank account.")
    public void getAccountInfo() {

        Optional<BankAccountDto> result = service.getAccountInfo("theodore.roosevelt@gmail.com");

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();

        assumingThat(bankAccountDto.active(), () -> assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance()));
        assertThat(bankAccountDto.accountId()).isEqualTo("theodore.roosevelt@gmail.com");
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
    }

    @Test
    @DisplayName("Test activate bank account.")
    public void activateAccount() {

        Optional<BankAccountDto> result = service.getAccountInfo("franklin.benjamin@gmail.com");

        assertThat(result.isPresent()).isTrue();

        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.active()).isFalse();

        result = service.activateAccount("franklin.benjamin@gmail.com");

        assertThat(result.isPresent()).isTrue();

        bankAccountDto = result.get();
        assertThat(bankAccountDto.active()).isTrue();
    }

    @Test
    @DisplayName("Test activate not-exists bank account.")
    public void activateAccount_WithNotExistsBankAccount() {
        assertThrows(EntityNotFoundException.class, () -> service.activateAccount("fake@gmail.com"));
    }

    @Test
    @DisplayName("Test deactivate bank account.")
    public void deactivateAccount() {

        Optional<BankAccountDto> result = service.getAccountInfo("theodore.roosevelt@gmail.com");

        assertThat(result.isPresent()).isTrue();

        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.active()).isTrue();

        result = service.deactivateAccount("theodore.roosevelt@gmail.com");

        assertThat(result.isPresent()).isTrue();

        bankAccountDto = result.get();
        assertThat(bankAccountDto.active()).isFalse();
    }

    @Test
    @DisplayName("Test deactivate not-exists bank account.")
    public void deactivateAccount_WithNotExistsBankAccount() {
        assertThrows(EntityNotFoundException.class, () -> service.deactivateAccount("fake@gmail.com"));
    }

    @Test
    @DisplayName("Test deposit to a bank account.")
    public void makeDeposit() {

        Optional<BankAccountDto> result = service.makeDeposit("theodore.roosevelt@gmail.com", 50);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3550);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());
    }

    @Test
    @DisplayName("Test deposit to a bank account and measure time.")
    public void makeDeposit_measureTime() {
        assertTimeout(Duration.ofMillis(60), () -> service.makeDeposit("theodore.roosevelt@gmail.com", 50));
    }

    @Test
    @DisplayName("Test deposit to a not-exists bank account, result=EntityNotFoundException")
    public void makeDeposit_WithNotExistsBankAccount() {
        assertThrows(EntityNotFoundException.class, () -> service.makeDeposit("fake@gmail.com", 50));
    }

    @Test
    @DisplayName("Test withdraw from a bank account.")
    public void makeWithdraw() {

        Optional<BankAccountDto> result = service.makeWithdraw("theodore.roosevelt@gmail.com", 1999);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(1501);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());
    }

    @Test
    @DisplayName("Test withdraw from a bank account until it run-out of the money.")
    public void makeWithdraw_BelowMinimum() {

        Optional<BankAccountDto> result = service.makeWithdraw("theodore.roosevelt@gmail.com", 1000);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(2500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());

        result = service.makeWithdraw("theodore.roosevelt@gmail.com", 1000);

        assertTrue(result.isPresent());
        bankAccountDto = result.get();
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance().doubleValue()).isEqualTo(bankAccountDto.minimumBalance().doubleValue());

        assertThrows(InsufficientFundsException.class, () -> service.makeWithdraw("theodore.roosevelt@gmail.com", 1000));
    }

    @Test
    @DisplayName("Test withdraw from a not-exists bank account. result=EntityNotFoundException")
    public void makeWithdraw_WithNotExistsBankAccount() {
        assertThrows(EntityNotFoundException.class, () -> service.makeWithdraw("fake@gmail.com", 50));
    }

    @Test
    @DisplayName("Test withdraw from a bank account with not enough money in his account. result=InsufficientFundsException")
    public void makeWithdraw_WithNInsufficientFundsException() {
        assertThrows(InsufficientFundsException.class, () -> service.makeWithdraw("theodore.roosevelt@gmail.com", 2001));
    }

    @Test
    @DisplayName("Test withdraw and deposit a few times for the same bank account. result=InsufficientFundsException")
    public void makeWithdraw_makeDeposit() {

        assertAll(() -> service.makeWithdraw("theodore.roosevelt@gmail.com", 100),
                () -> service.makeWithdraw("theodore.roosevelt@gmail.com", 100),
                () -> service.makeWithdraw("theodore.roosevelt@gmail.com", 100),
                () -> service.makeDeposit("theodore.roosevelt@gmail.com", 1000),
                () -> service.makeWithdraw("theodore.roosevelt@gmail.com", 100),
                () -> service.makeWithdraw("theodore.roosevelt@gmail.com", 100));

        Optional<BankAccountDto> result = service.makeWithdraw("theodore.roosevelt@gmail.com", 1);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3999);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());
    }
}