package com.mlustig.bank_management.dto;

import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountPropertiesDto(
        @NonNull BigDecimal minimumBalance,
        @NonNull Boolean active,
        @NonNull LocalDateTime updateAt) {
}