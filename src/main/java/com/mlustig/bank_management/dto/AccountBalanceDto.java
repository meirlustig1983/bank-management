package com.mlustig.bank_management.dto;

import lombok.NonNull;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountBalanceDto(
        @NonNull BigDecimal balance,
        @NonNull @With LocalDateTime updatedAt) {
}