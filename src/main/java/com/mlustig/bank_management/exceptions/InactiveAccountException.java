package com.mlustig.bank_management.exceptions;

public class InactiveAccountException extends RuntimeException {
    public InactiveAccountException() {
        super("Inactive bank account");
    }
}