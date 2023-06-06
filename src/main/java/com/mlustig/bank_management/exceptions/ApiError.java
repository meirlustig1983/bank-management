package com.mlustig.bank_management.exceptions;

public record ApiError(String path, String message, int statusCode) {
}
