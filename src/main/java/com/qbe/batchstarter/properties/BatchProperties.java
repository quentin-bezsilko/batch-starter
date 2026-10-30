package com.qbe.batchstarter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "batch")
public record BatchProperties(int chunkSize, int parallelThreads, int partitioningGridSize, int skipLimit) {}
