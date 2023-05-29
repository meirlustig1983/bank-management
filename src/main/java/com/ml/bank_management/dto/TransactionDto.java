package com.ml.bank_management.dto;

import com.ml.bank_management.enums.TransactionType;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
        @NonNull BigDecimal amount,
        @NonNull TransactionType type,
        @NonNull LocalDateTime createdAt) {
}