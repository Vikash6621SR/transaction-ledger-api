package com.example.ledger.controller;

import com.example.ledger.dto.CreateTransactionRequest;
import com.example.ledger.dto.TransactionHistoryResponse;
import com.example.ledger.entity.LedgerTransaction;
import com.example.ledger.service.TransactionHistoryService;
import com.example.ledger.service.TransactionService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionHistoryService historyService;

    public TransactionController(
            TransactionService transactionService,
            TransactionHistoryService historyService) {
        this.transactionService = transactionService;
        this.historyService = historyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LedgerTransaction createTransaction(
            @PathVariable Long accountId,
            @Valid @RequestBody CreateTransactionRequest request) {

        return transactionService.createTransaction(accountId, request);
    }

    @GetMapping
    public TransactionHistoryResponse getTransactionHistory(
            @PathVariable Long accountId,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {

        LedgerTransaction.TransactionType transactionType = null;

        if (type != null && !type.isBlank()) {
            try {
                transactionType =
                        LedgerTransaction.TransactionType.valueOf(
                                type.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Invalid transaction type. Use CREDIT or DEBIT.");
            }
        }

        return historyService.getHistory(
                accountId, transactionType, page, limit);
    }
}
