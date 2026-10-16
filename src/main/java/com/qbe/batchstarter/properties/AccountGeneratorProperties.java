package com.qbe.batchstarter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "generator.accounts")
public record AccountGeneratorProperties(
        int singleAccountRate,
        int doubleAccountRate,
        int tripleAccountRate,
        int checkingRate,
        int savingsRate,
        int businessRate,
        int activeRate,
        int blockedRate,
        int closedRate,
        double minBalance,
        double maxBalance,
        int maxOpeningAgeInDays) {}
