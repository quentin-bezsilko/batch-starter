package com.qbe.batchstarter.dto;

import java.math.BigDecimal;

public record CustomerEnrichmentDto(
        Long customerId,
        String customerName,
        int accountCount,
        BigDecimal totalBalance,
        long transactionCount,
        BigDecimal totalCredit,
        BigDecimal totalDebit) {}
