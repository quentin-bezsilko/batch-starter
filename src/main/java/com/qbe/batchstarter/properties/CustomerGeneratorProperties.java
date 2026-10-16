package com.qbe.batchstarter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "generator.customers")
public record CustomerGeneratorProperties(int count, int minAge, int maxAge, double minorRate, double majorRate) {}
