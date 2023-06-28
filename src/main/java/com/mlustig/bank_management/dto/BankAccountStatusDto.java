package com.mlustig.bank_management.dto;

import jakarta.validation.constraints.Email;
import lombok.NonNull;

import java.math.BigDecimal;

public record BankAccountStatusDto(
        @Email(message = "Invalid email format") @NonNull String accountId,
        boolean active) {

}