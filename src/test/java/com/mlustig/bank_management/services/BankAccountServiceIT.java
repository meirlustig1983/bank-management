package com.mlustig.bank_management.services;

import com.mlustig.bank_management.dto.BankAccountDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@Tag("integration")
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
    @DisplayName("Test delete a bank account.")
    public void deleteBankAccountByAccountId() {
        // Delete an existing bank account
        service.deleteBankAccountByAccountId("theodore.roosevelt@gmail.com");

        // Verify that the bank account is deleted by trying to retrieve it
        assertThrows(EntityNotFoundException.class, () -> service.getAccountInfo("theodore.roosevelt@gmail.com"));
    }
}