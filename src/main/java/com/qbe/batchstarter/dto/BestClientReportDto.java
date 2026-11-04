package com.qbe.batchstarter.dto;

import java.math.BigDecimal;

public record BestClientReportDto(Long customerId, String customerName, long accountCount, BigDecimal totalBalance) {}
