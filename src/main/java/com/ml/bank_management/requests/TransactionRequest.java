package com.ml.bank_management.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;

public record TransactionRequest(
        @Email(message = "Invalid email format") @NonNull String accountId,
        @Positive(message = "Amount must be a positive number") double amount) {
}