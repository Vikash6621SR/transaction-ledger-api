package com.example.ledger.dto;

import com.example.ledger.entity.LedgerTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record TransactionHistoryResponse(
        Long accountId,
        int currentPage,
        int pageSize,
        long totalRecords,
        int totalPages,
        List<TransactionItem> transactions
) {

    public record TransactionItem(
            Long id,
            Long accountId,
            String type,
            BigDecimal amount,
            String description,
            String referenceId,
            LocalDateTime createdAt
    ) {
        public static TransactionItem from(LedgerTransaction transaction) {
            return new TransactionItem(
                    transaction.getId(),
                    transaction.getAccount().getId(),
                    transaction.getType().name(),
                    transaction.getAmount(),
                    transaction.getDescription(),
                    transaction.getReferenceId(),
                    transaction.getCreatedAt()
            );
        }
    }
}
