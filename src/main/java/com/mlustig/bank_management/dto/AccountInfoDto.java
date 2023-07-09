package com.mlustig.bank_management.dto;

import jakarta.validation.constraints.Email;
import lombok.NonNull;

import java.time.LocalDateTime;

public record AccountInfoDto(
        @Email(message = "Invalid email format") @NonNull String userName,
        @NonNull String firstName,
        @NonNull String lastName,
        @Email(message = "Invalid email format") @NonNull String email,
        @NonNull String phoneNumber) {
}