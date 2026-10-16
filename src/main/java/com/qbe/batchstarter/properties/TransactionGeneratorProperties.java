package com.qbe.batchstarter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "generator.transactions")
public record TransactionGeneratorProperties(
        int count,
        int cardPaymentRate,
        int transferRate,
        int directDebitRate,
        int cashWithdrawalRate,
        int completedRate,
        int pendingRate,
        int rejectedRate,
        double highAmountFraudRate) {}
