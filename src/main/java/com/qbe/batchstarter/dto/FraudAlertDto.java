package com.qbe.batchstarter.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FraudAlertDto(
        Long transactionId,
        Long accountId,
        String fraudType,
        BigDecimal amount,
        String reason,
        LocalDateTime detectionDate) {}
