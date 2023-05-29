package com.ml.bank_management.mappers;

import com.ml.bank_management.dao.Transaction;
import com.ml.bank_management.dto.TransactionDto;
import com.ml.bank_management.enums.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionMapperTest {

    @Test
    public void testToDto() {
        // Create a Transaction object for testing
        Transaction transaction = Transaction.builder()
                .id(1L)
                .bankAccountId(123L)
                .amount(BigDecimal.valueOf(100.00))
                .type(TransactionType.DEPOSIT)
                .createdAt(LocalDateTime.now())
                .build();

        // Convert Transaction to TransactionDto using the mapper
        TransactionDto transactionDto = TransactionMapper.INSTANCE.toDto(transaction);

        // Assert that the fields are mapped correctly
        Assertions.assertEquals(transaction.getAmount(), transactionDto.amount());
        Assertions.assertEquals(transaction.getType(), transactionDto.type());
        Assertions.assertEquals(transaction.getCreatedAt(), transactionDto.createdAt());
    }
}
