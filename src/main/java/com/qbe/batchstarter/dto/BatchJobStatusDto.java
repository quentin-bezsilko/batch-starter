package com.qbe.batchstarter.dto;

import java.time.LocalDateTime;

public record BatchJobStatusDto(
        String jobName, Long executionId, String status, LocalDateTime startTime, LocalDateTime endTime) {}
