package com.mlustig.bank_management.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;

public record CreditLimitRequest(
        @Email(message = "Invalid email format") @NonNull String userName,
        double amount) {
}