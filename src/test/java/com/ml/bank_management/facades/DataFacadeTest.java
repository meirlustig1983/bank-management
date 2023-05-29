package com.ml.bank_management.facades;

import com.ml.bank_management.dao.BankAccount;
import com.ml.bank_management.enums.BankAccountFields;
import com.ml.bank_management.repositories.BankAccountRepository;
import com.ml.bank_management.utils.CustomDisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.util.Pair;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
@DisplayNameGeneration(CustomDisplayNameGenerator.class)
public class DataFacadeTest {

    @Mock
    private BankAccountRepository repository;

    @InjectMocks
    private DataFacade dataFacade;

    @Test
    public void findAllBankAccounts() {
        // Arrange
        BankAccount bankAccount1 = createBankAccount(1L, "theodore.roosevelt@gmail.com", "Theodore", "Roosevelt",
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500));
        BankAccount bankAccount2 = createBankAccount(2L, "franklin.benjamin@gmail.com", "Franklin", "Benjamin",
                BigDecimal.valueOf(0), BigDecimal.valueOf(-1000));
        when(repository.findAll()).thenReturn(List.of(bankAccount1, bankAccount2));

        // Act
        List<BankAccount> result = dataFacade.findAllBankAccounts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertBankAccountEquals(bankAccount1, result.get(0));
        assertBankAccountEquals(bankAccount2, result.get(1));
        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findBankAccountByAccountId_ExistingAccountId_ReturnsBankAccount() {
        // Arrange
        BankAccount bankAccount = createBankAccount(1L, "theodore.roosevelt@gmail.com", "Theodore", "Roosevelt",
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500));
        when(repository.findBankAccountByAccountId("theodore.roosevelt@gmail.com"))
                .thenReturn(Optional.of(bankAccount));

        // Act
        Optional<BankAccount> result = dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com");

        // Assert
        assertTrue(result.isPresent());
        assertBankAccountEquals(bankAccount, result.get());
        verify(repository).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void findBankAccountByAccountId_NonExistingAccountId_ReturnsEmptyOptional() {
        // Arrange
        when(repository.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.empty());

        // Act
        Optional<BankAccount> result = dataFacade.findBankAccountByAccountId("theodore.roosevelt@gmail.com");

        // Assert
        assertFalse(result.isPresent());
        verify(repository).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void saveBankAccount_ValidBankAccount_ReturnsSavedBankAccount() {
        // Arrange
        BankAccount bankAccount = createBankAccount(1L, "theodore.roosevelt@gmail.com", "Theodore", "Roosevelt",
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500));
        when(repository.save(bankAccount)).thenReturn(bankAccount);

        // Act
        Optional<BankAccount> result = dataFacade.saveBankAccount(bankAccount);

        // Assert
        assertTrue(result.isPresent());
        assertBankAccountEquals(bankAccount, result.get());
        verify(repository).save(bankAccount);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void updateBankAccount_ExistingAccountIdAndValidFields_ReturnsUpdatedBankAccount() {
        // Arrange
        BankAccount originalBankAccount = createBankAccount(1L, "theodore.roosevelt@gmail.com", "Theodore", "Roosevelt",
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500));
        BankAccount updatedBankAccount = createBankAccount(1L, "theodore.roosevelt@gmail.com", "Meir", "Roth",
                BigDecimal.valueOf(10000), BigDecimal.valueOf(0));

        when(repository.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.of(originalBankAccount));
        when(repository.save(any(BankAccount.class))).thenReturn(updatedBankAccount);

        // Act
        Optional<BankAccount> result = dataFacade.updateBankAccount("theodore.roosevelt@gmail.com",
                List.of(
                        Pair.of(BankAccountFields.FIRST_NAME, "Meir"),
                        Pair.of(BankAccountFields.LAST_NAME, "Roth"),
                        Pair.of(BankAccountFields.BALANCE, "10000"),
                        Pair.of(BankAccountFields.MINIMUM_BALANCE, "0")
                ));

        // Assert
        assertTrue(result.isPresent());
        assertBankAccountEquals(updatedBankAccount, result.get());
        verify(repository).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verify(repository).save(any(BankAccount.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void updateBankAccount_NonExistingAccountId_ReturnsEmptyOptional() {
        // Arrange
        when(repository.findBankAccountByAccountId("fake@gmail.com")).thenReturn(Optional.empty());

        // Act
        Optional<BankAccount> result = dataFacade.updateBankAccount("fake@gmail.com",
                List.of(Pair.of(BankAccountFields.BALANCE, "1000"), Pair.of(BankAccountFields.BALANCE, "15000")));

        // Assert
        assertFalse(result.isPresent());
        verify(repository).findBankAccountByAccountId("fake@gmail.com");
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void updateBankAccount_UnauthorizedField_ThrowsIllegalArgumentException() {
        // Arrange
        BankAccount originalBankAccount = createBankAccount(1L, "theodore.roosevelt@gmail.com", "Theodore", "Roosevelt",
                BigDecimal.valueOf(3500), BigDecimal.valueOf(1500));
        when(repository.findBankAccountByAccountId("theodore.roosevelt@gmail.com")).thenReturn(Optional.of(originalBankAccount));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> dataFacade.updateBankAccount("theodore.roosevelt@gmail.com",
                List.of(
                        Pair.of(BankAccountFields.ID, "1000"),
                        Pair.of(BankAccountFields.BALANCE, "8500")
                )));
        verify(repository).findBankAccountByAccountId("theodore.roosevelt@gmail.com");
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void deleteBankAccountById_ValidAccountId_DeletesBankAccount() {
        // Act
        dataFacade.deleteBankAccountByAccountId("theodore.roosevelt@gmail.com");

        // Assert
        verify(repository).deleteByAccountId("theodore.roosevelt@gmail.com");
        verifyNoMoreInteractions(repository);
    }

    // Helper methods

    private BankAccount createBankAccount(Long id, String accountId, String firstName, String lastName,
                                          BigDecimal balance, BigDecimal minimumBalance) {
        return BankAccount.builder()
                .id(id)
                .accountId(accountId)
                .firstName(firstName)
                .lastName(lastName)
                .balance(balance)
                .minimumBalance(minimumBalance)
                .build();
    }

    private void assertBankAccountEquals(BankAccount expected, BankAccount actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAccountId(), actual.getAccountId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getBalance(), actual.getBalance());
        assertEquals(expected.getMinimumBalance(), actual.getMinimumBalance());
        assertInstanceOf(LocalDateTime.class, actual.getCreatedAt());
        assertInstanceOf(LocalDateTime.class, actual.getUpdatedAt());
    }
}
