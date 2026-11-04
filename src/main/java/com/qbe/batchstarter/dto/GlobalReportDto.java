package com.qbe.batchstarter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GlobalReportDto(
        LocalDate reportDate,
        long totalCustomers,
        long totalAccounts,
        long totalTransactions,
        long totalFrauds,
        BigDecimal totalBalance) {}
