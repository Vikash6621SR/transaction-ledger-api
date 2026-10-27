package com.example.ledger.dto;

import com.example.ledger.entity.LedgerTransaction.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateTransactionRequest(

        @NotNull(message = "Transaction type is required")
        TransactionType type,

        @NotNull(message = "Amount is required")
        @DecimalMin(
                value = "0.00",
                inclusive = false,
                message = "Amount must be greater than zero"
        )
        @Digits(
                integer = 17,
                fraction = 2,
                message = "Amount must have at most 17 integer digits and 2 decimal places"
        )
        BigDecimal amount,

        @Size(
                max = 255,
                message = "Description cannot exceed 255 characters"
        )
        String description,

        @NotBlank(message = "Reference ID is required")
        @Size(
                max = 100,
                message = "Reference ID cannot exceed 100 characters"
        )
        String referenceId

) {
}
