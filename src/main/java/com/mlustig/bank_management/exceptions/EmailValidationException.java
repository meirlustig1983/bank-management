package com.mlustig.bank_management.exceptions;

public class EmailValidationException extends RuntimeException {
    public EmailValidationException() {
        super("Wrong format exception");
    }
}