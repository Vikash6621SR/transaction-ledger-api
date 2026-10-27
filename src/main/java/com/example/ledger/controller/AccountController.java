package com.example.ledger.controller;

import com.example.ledger.dto.AccountBalanceResponse;
import com.example.ledger.dto.CreateAccountRequest;
import com.example.ledger.entity.Account;
import com.example.ledger.service.AccountService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @GetMapping("/{id}/balance")
    public AccountBalanceResponse getBalance(@PathVariable Long id) {
        Account account = accountService.getAccount(id);

        return new AccountBalanceResponse(
                account.getId(),
                account.getBalance()
        );
    }
}
