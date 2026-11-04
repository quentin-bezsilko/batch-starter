package com.qbe.batchstarter.dto;

import java.math.BigDecimal;

public record CustomerReportExportDto(
        Long customerId,
        String customerName,
        String country,
        Integer age,
        Long accountCount,
        BigDecimal totalBalance) {}
