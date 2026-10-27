package com.example.ledger.service;

import com.example.ledger.dto.CreateTransactionRequest;
import com.example.ledger.entity.Account;
import com.example.ledger.entity.LedgerTransaction;
import com.example.ledger.entity.LedgerTransaction.TransactionType;
import com.example.ledger.repository.AccountRepository;
import com.example.ledger.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public LedgerTransaction createTransaction(
            Long accountId,
            CreateTransactionRequest request) {

        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found with ID: " + accountId));

        String referenceId = request.referenceId().trim();

        if (transactionRepository.existsByAccount_IdAndReferenceId(
                accountId, referenceId)) {
            throw new DuplicateTransactionException(
                    "Transaction already exists for this account and reference ID");
        }

        BigDecimal amount = request.amount();

        if (amount.scale() > 2) {
            throw new IllegalArgumentException(
                    "Amount cannot have more than 2 decimal places");
        }

        BigDecimal newBalance;

        if (request.type() == TransactionType.CREDIT) {
            newBalance = account.getBalance().add(amount);
        } else if (request.type() == TransactionType.DEBIT) {
            if (account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientBalanceException(
                        "Insufficient balance for this debit transaction");
            }

            newBalance = account.getBalance().subtract(amount);
        } else {
            throw new IllegalArgumentException("Invalid transaction type");
        }

        LedgerTransaction transaction = new LedgerTransaction(
                account,
                request.type(),
                amount,
                request.description(),
                referenceId
        );

        account.setBalance(newBalance);

        accountRepository.save(account);

        return transactionRepository.saveAndFlush(transaction);
    }

    public static class DuplicateTransactionException
            extends RuntimeException {
        public DuplicateTransactionException(String message) {
            super(message);
        }
    }

    public static class InsufficientBalanceException
            extends RuntimeException {
        public InsufficientBalanceException(String message) {
            super(message);
        }
    }
}
