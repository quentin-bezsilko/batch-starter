package com.qbe.batchstarter.dto;

import java.math.BigDecimal;

public record FraudReportDto(String fraudType, long fraudCount, BigDecimal totalAmount) {}
