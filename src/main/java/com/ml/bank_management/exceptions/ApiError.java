package com.ml.bank_management.exceptions;

public record ApiError(String path, String message, int statusCode) {
}
