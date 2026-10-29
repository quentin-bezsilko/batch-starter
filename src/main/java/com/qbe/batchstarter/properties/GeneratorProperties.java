package com.qbe.batchstarter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "generator")
public record GeneratorProperties(
        boolean simulateRetry,
        String outputDirectory,
        String customerFileName,
        String accountFileName,
        String transactionFileName) {}
