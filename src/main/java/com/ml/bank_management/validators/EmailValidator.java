package com.ml.bank_management.validators;

public class EmailValidator {
    public static boolean isValid(String email) {
        // Regular expression for email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return !email.matches(emailRegex);
    }
}