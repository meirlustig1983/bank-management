package com.ml.bank_management.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        return createApiError(request, e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(InactiveAccountException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInactiveAccountException(InactiveAccountException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        return createApiError(request, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        return createApiError(request, "Internal SQL error", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInsufficientFundsException(InsufficientFundsException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        return createApiError(request, e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        String fieldName = e.getMessage().contains("accountId") ? "accountId" : "amount";
        String errorMessage = "Request validation exception [" + "field: " + fieldName + "]";
        return createApiError(request, errorMessage, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        return createApiError(request, "Wrong field type exception", HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(EmailValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEmailValidationException(EmailValidationException e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. ", e);
        return createApiError(request, e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleExceptions(Exception e, HttpServletRequest request) {
        log.error("Unhandled exception occurred", e);
        return createApiError(request, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ApiError createApiError(HttpServletRequest request, String message, int statusCode) {
        return new ApiError(request.getRequestURI(), message, statusCode);
    }
}
