package com.qbe.batchstarter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "generator")
public record GeneratorProperties(
        String outputDirectory, String customerFileName, String accountFileName, String transactionFileName) {}
