package com.qbe.batchstarter.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CustomerReportDto(
        Long customerId,
        String customerName,
        String country,
        LocalDate birthDate,
        Long accountCount,
        BigDecimal totalBalance) {}
