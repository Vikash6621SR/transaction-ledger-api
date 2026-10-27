package com.example.ledger.service;

import com.example.ledger.dto.TransactionHistoryResponse;
import com.example.ledger.entity.Account;
import com.example.ledger.entity.LedgerTransaction;
import com.example.ledger.repository.AccountRepository;
import com.example.ledger.repository.TransactionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionHistoryService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionHistoryService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public TransactionHistoryResponse getHistory(
            Long accountId,
            LedgerTransaction.TransactionType type,
            int page,
            int limit) {

        if (page < 1) {
            throw new IllegalArgumentException("Page must be at least 1");
        }

        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException(
                    "Limit must be between 1 and 100");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found with ID: " + accountId));

        PageRequest pageable = PageRequest.of(
                page - 1,
                limit,
                Sort.by(
                        Sort.Order.desc("createdAt"),
                        Sort.Order.desc("id")
                )
        );

        Page<LedgerTransaction> results = (type == null)
                ? transactionRepository.findByAccount_Id(accountId, pageable)
                : transactionRepository.findByAccount_IdAndType(
                        accountId, type, pageable);

        return new TransactionHistoryResponse(
                account.getId(),
                page,
                limit,
                results.getTotalElements(),
                results.getTotalPages(),
                results.getContent().stream()
                        .map(TransactionHistoryResponse.TransactionItem::from)
                        .toList()
        );
    }
}
