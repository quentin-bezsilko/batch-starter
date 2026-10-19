package com.qbe.batchstarter.dto;

import com.qbe.batchstarter.enums.TransactionStatus;
import com.qbe.batchstarter.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BankTransactionDto(
        Long id,
        Long accountId,
        LocalDateTime transactionDate,
        TransactionType transactionType,
        BigDecimal amount,
        String merchantName,
        String currency,
        TransactionStatus transactionStatus) {}
