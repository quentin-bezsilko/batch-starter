package com.qbe.batchstarter.dto;

import com.qbe.batchstarter.enums.TransactionType;
import java.math.BigDecimal;

public record TransactionReportDto(
        TransactionType transactionType, Long transactionCount, BigDecimal totalAmount, Double averageAmount) {}
