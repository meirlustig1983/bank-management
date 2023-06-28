package com.mlustig.bank_management.services;

import com.mlustig.bank_management.dao.BankAccount;
import com.mlustig.bank_management.dto.BankAccountDto;
import com.mlustig.bank_management.facades.DataFacade;
import com.mlustig.bank_management.mappers.BankAccountMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.*;

@MockitoSettings
public class BankAccountServiceTest {

    @Mock
    private DataFacade dataFacade;

    @Mock
    private BankAccountMapper mapper;

    @InjectMocks
    private BankAccountService service;

    @Test
    @DisplayName("Test get an info about bank account and run some tests it the account is active.")
    public void getAccountInfo_GetInfoForBankAccount() {

        BankAccount original = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        BankAccountDto originalBankAccountDto = new BankAccountDto("theodore.roosevelt@gmail.com",
                "Theodore", "Roosevelt",
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500), true);

        when(dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.of(original));
        when(mapper.toDto(original)).thenReturn(originalBankAccountDto);

        Optional<BankAccountDto> result = service.getAccountInfo("theodore.roosevelt@gmail.com");

        assertTrue(result.isPresent());
        assumeTrue(result.get().active(), "The account is not active");
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.accountId()).isEqualTo("theodore.roosevelt@gmail.com");
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());

        verify(dataFacade).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test get an info about bank account for invalid bank account.")
    public void getAccountInfo_GetInfoForInvalidBankAccount() {
        when(dataFacade.findBankAccountByAccountId("fake@gmail.com")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getAccountInfo("fake@gmail.com"));

        verify(dataFacade).findBankAccountByAccountId("fake@gmail.com");
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test create new bank account.")
    public void createAccount() {

        BankAccount originalBankAccount = BankAccount.builder()
                .id(3L)
                .accountId("theodore.roosevelt.2@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        BankAccountDto originalBankAccountDto = new BankAccountDto("theodore.roosevelt.2@gmail.com",
                "Theodore", "Roosevelt",
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500), true);

        when(dataFacade.saveBankAccount(originalBankAccount)).thenReturn(Optional.of(originalBankAccount));
        when(mapper.toDto(originalBankAccount)).thenReturn(originalBankAccountDto);
        when(mapper.toDao(originalBankAccountDto)).thenReturn(originalBankAccount);

        Optional<BankAccountDto> result = service.createAccount(originalBankAccountDto);

        assertTrue(result.isPresent());
        assumeTrue(result.get().active(), "The account is not active");
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.accountId()).isEqualTo("theodore.roosevelt.2@gmail.com");
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());

        verify(dataFacade).saveBankAccount(originalBankAccount);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test delete bank account by account id.")
    public void deleteBankAccountByAccountId() {
        service.deleteBankAccountByAccountId("fake@gmail.com");
        verify(dataFacade).deleteBankAccountByAccountId("fake@gmail.com");
        verifyNoMoreInteractions(dataFacade);
    }
}