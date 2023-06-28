package com.mlustig.bank_management.services;

import com.mlustig.bank_management.dao.BankAccount;
import com.mlustig.bank_management.dto.BankAccountDto;
import com.mlustig.bank_management.enums.BankAccountFields;
import com.mlustig.bank_management.enums.TransactionType;
import com.mlustig.bank_management.exceptions.InactiveAccountException;
import com.mlustig.bank_management.exceptions.InsufficientFundsException;
import com.mlustig.bank_management.facades.DataFacade;
import com.mlustig.bank_management.mappers.BankAccountMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@MockitoSettings
class BankManagementServiceTest {

    @Mock
    private DataFacade dataFacade;

    @Mock
    private BankAccountMapper mapper;

    @InjectMocks
    private BankManagementService service;

    @Test
    @DisplayName("Test activate bank account")
    public void activateAccount() {

        BankAccount original = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(false)
                .build();

        BankAccount updated = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        BankAccountDto updatedBankAccountDto = new BankAccountDto("theodore.roosevelt@gmail.com",
                "Theodore", "Roosevelt",
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500), true);

        when(dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.of(original));
        when(dataFacade.updateBankAccount("theodore.roosevelt@gmail.com", List.of(Pair.of(BankAccountFields.ACTIVE, "true")))).thenReturn(Optional.of(updated));
        when(mapper.toDto(updated)).thenReturn(updatedBankAccountDto);

        Optional<BankAccountDto> result = service.activateAccount("theodore.roosevelt@gmail.com");

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.accountId()).isEqualTo("theodore.roosevelt@gmail.com");
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.active()).isTrue();
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());

        verify(dataFacade).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verify(dataFacade).updateBankAccount("theodore.roosevelt@gmail.com", List.of(Pair.of(BankAccountFields.ACTIVE, "true")));
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test activate active bank account")
    public void activateActiveAccount() {

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

        Optional<BankAccountDto> result = service.activateAccount("theodore.roosevelt@gmail.com");

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.accountId()).isEqualTo("theodore.roosevelt@gmail.com");
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.active()).isTrue();
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());

        verify(dataFacade).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verify(dataFacade, never()).updateBankAccount("theodore.roosevelt@gmail.com", List.of(Pair.of(BankAccountFields.ACTIVE, "true")));
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test activate on to not-exists bank account")
    public void activateAccount_WithNotExistsBankAccount() {
        when(dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.activateAccount("theodore.roosevelt@gmail.com"));
        verify(dataFacade).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test deactivate bank account.")
    public void deactivateAccount() {
        BankAccount original = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        BankAccount updated = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(false)
                .build();

        BankAccountDto updatedBankAccountDto = new BankAccountDto("theodore.roosevelt@gmail.com",
                "Theodore", "Roosevelt",
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500), false);

        when(dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.of(original));
        when(dataFacade.updateBankAccount("theodore.roosevelt@gmail.com", List.of(Pair.of(BankAccountFields.ACTIVE, "false")))).thenReturn(Optional.of(updated));
        when(mapper.toDto(updated)).thenReturn(updatedBankAccountDto);

        Optional<BankAccountDto> result = service.deactivateAccount("theodore.roosevelt@gmail.com");

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.accountId()).isEqualTo("theodore.roosevelt@gmail.com");
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3500);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.active()).isFalse();
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());

        verify(dataFacade).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verify(dataFacade).updateBankAccount("theodore.roosevelt@gmail.com", List.of(Pair.of(BankAccountFields.ACTIVE, "false")));
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test deactivate on to not-exists bank account.")
    public void deactivateAccount_WithNotExistsBankAccount() {
        when(dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.deactivateAccount("theodore.roosevelt@gmail.com"));
        verify(dataFacade).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test deposit to bank account.")
    public void makeDeposit() {
        BankAccount original = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        BankAccount updated = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3550))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        BankAccountDto updatedBankAccountDto = new BankAccountDto("theodore.roosevelt@gmail.com",
                "Theodore", "Roosevelt",
                BigDecimal.valueOf(3550), BigDecimal.valueOf(1500), true);

        when(dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.of(original));
        when(dataFacade.updateBankAccount("theodore.roosevelt@gmail.com", List.of(Pair.of(BankAccountFields.BALANCE, "3550.0")))).thenReturn(Optional.of(updated));
        when(mapper.toDto(updated)).thenReturn(updatedBankAccountDto);

        Optional<BankAccountDto> result = service.makeDeposit("theodore.roosevelt@gmail.com", 50);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.accountId()).isEqualTo("theodore.roosevelt@gmail.com");
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(3550);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());

        verify(dataFacade).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verify(dataFacade).updateBankAccount("theodore.roosevelt@gmail.com", List.of(Pair.of(BankAccountFields.BALANCE, "3550.0")));
        verify(dataFacade).saveTransaction(1L, BigDecimal.valueOf(50.0), TransactionType.DEPOSIT);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test deposit to not-exists bank account. result=EntityNotFoundException")
    public void makeDeposit_WithNotExistsBankAccount() {
        when(dataFacade.findBankAccountByAccountId("fake@gmail.com")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.makeDeposit("fake@gmail.com", 50));
        verify(dataFacade).findBankAccountByAccountId("fake@gmail.com");
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test deposit to inactive bank account. result=InactiveAccountException")
    public void makeDeposit_WithInactiveAccount() {
        BankAccount bankAccount = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(false)
                .build();

        when(dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.of(bankAccount));
        assertThrows(InactiveAccountException.class, () -> service.makeDeposit("theodore.roosevelt@gmail.com", 50));
        verify(dataFacade).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test withdraw from a bank account.")
    public void withdraw() {
        BankAccount original = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        BankAccount updated = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(1501))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        BankAccountDto updatedBankAccountDto = new BankAccountDto("theodore.roosevelt@gmail.com",
                "Theodore", "Roosevelt",
                BigDecimal.valueOf(1501), BigDecimal.valueOf(1500), true);

        when(dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.of(original));
        when(dataFacade.updateBankAccount("theodore.roosevelt@gmail.com",
                List.of(Pair.of(BankAccountFields.BALANCE, "1501.0")))).thenReturn(Optional.of(updated));
        when(mapper.toDto(updated)).thenReturn(updatedBankAccountDto);

        Optional<BankAccountDto> result = service.makeWithdraw("theodore.roosevelt@gmail.com", 1999);

        assertTrue(result.isPresent());
        BankAccountDto bankAccountDto = result.get();
        assertThat(bankAccountDto.accountId()).isEqualTo("theodore.roosevelt@gmail.com");
        assertThat(bankAccountDto.firstName()).isEqualTo("Theodore");
        assertThat(bankAccountDto.lastName()).isEqualTo("Roosevelt");
        assertThat(bankAccountDto.balance().intValue()).isEqualTo(1501);
        assertThat(bankAccountDto.minimumBalance().intValue()).isEqualTo(1500);
        assertThat(bankAccountDto.balance()).isGreaterThan(bankAccountDto.minimumBalance());

        verify(dataFacade).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verify(dataFacade).updateBankAccount("theodore.roosevelt@gmail.com",
                List.of(Pair.of(BankAccountFields.BALANCE, "1501.0")));
        verify(dataFacade).saveTransaction(1L, BigDecimal.valueOf(1999.0), TransactionType.WITHDRAW);
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test withdraw from not-exists bank account. result=EntityNotFoundException")
    public void makeWithdraw_WithNotExistsBankAccount() {
        when(dataFacade.findBankAccountByAccountId("fake@gmail.com")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.makeWithdraw("fake@gmail.com", 50));
        verify(dataFacade).findBankAccountByAccountId("fake@gmail.com");
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test withdraw from inactive bank account. result=InactiveAccountException")
    public void makeWithdraw_WithInactiveAccount() {
        BankAccount bankAccount = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(false)
                .build();

        when(dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.of(bankAccount));
        assertThrows(InactiveAccountException.class, () ->
                service.makeWithdraw("theodore.roosevelt@gmail.com", 50));
        verify(dataFacade).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verifyNoMoreInteractions(dataFacade);
    }

    @Test
    @DisplayName("Test a withdraw from a bank account with not enough money. result=InsufficientFundsException")
    public void makeWithdraw_WithNInsufficientFundsException() {
        BankAccount original = BankAccount.builder()
                .id(1L)
                .accountId("theodore.roosevelt@gmail.com")
                .firstName("Theodore")
                .lastName("Roosevelt")
                .balance(BigDecimal.valueOf(3500))
                .minimumBalance(BigDecimal.valueOf(1500))
                .active(true)
                .build();

        when(dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.of(original));
        assertThrows(InsufficientFundsException.class, () ->
                service.makeWithdraw("theodore.roosevelt@gmail.com", 2001));

        verify(dataFacade).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verifyNoMoreInteractions(dataFacade);
    }
}