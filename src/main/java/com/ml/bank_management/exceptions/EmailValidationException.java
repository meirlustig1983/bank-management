package com.ml.bank_management.exceptions;

public class EmailValidationException extends RuntimeException {
    public EmailValidationException() {
        super("Wrong format exception");
    }
}