package com.example.ledger.dto;

import java.math.BigDecimal;

public record AccountBalanceResponse(
        Long accountId,
        BigDecimal balance
) {
}
