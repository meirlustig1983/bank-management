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
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500), true, List.of());

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
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500), true, List.of());

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
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500), true, List.of());

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
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500), true, List.of());

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
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500), false, List.of());

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
                BigDecimal.valueOf(3550), BigDecimal.valueOf(1500), true, List.of());

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
                BigDecimal.valueOf(1501), BigDecimal.valueOf(1500), true, List.of());

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