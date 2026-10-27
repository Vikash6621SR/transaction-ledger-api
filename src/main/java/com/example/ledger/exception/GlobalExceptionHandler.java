package com.example.ledger.exception;

import com.example.ledger.service.TransactionService.DuplicateTransactionException;
import com.example.ledger.service.TransactionService.InsufficientBalanceException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> error(
            HttpStatus status, String code, String message) {

        return ResponseEntity.status(status).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "error", code,
                "message", message
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex) {

        if (ex.getMessage() != null
                && ex.getMessage().startsWith("Account not found")) {
            return error(HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND",
                    ex.getMessage());
        }

        return error(HttpStatus.BAD_REQUEST, "INVALID_REQUEST",
                ex.getMessage() == null ? "Invalid request" : ex.getMessage());
    }

    @ExceptionHandler(DuplicateTransactionException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(
            DuplicateTransactionException ex) {
        return error(HttpStatus.CONFLICT, "DUPLICATE_TRANSACTION",
                ex.getMessage());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientBalance(
            InsufficientBalanceException ex) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, "INSUFFICIENT_BALANCE",
                ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Request validation failed");

        return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJson(
            HttpMessageNotReadableException ex) {
        return error(HttpStatus.BAD_REQUEST, "INVALID_REQUEST_BODY",
                "Invalid JSON or transaction type. Use CREDIT or DEBIT.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleIntegrityViolation(
            DataIntegrityViolationException ex) {
        return error(HttpStatus.CONFLICT, "DATA_CONFLICT",
                "The request conflicts with existing database data.");
    }
}