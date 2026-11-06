package com.qbe.batchstarter.controller;

import com.qbe.batchstarter.dto.BatchJobStatusDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch/status")
@RequiredArgsConstructor
public class BatchStatusController {

    private final JobRepository jobRepository;

    @GetMapping("/running")
    public List<String> getRunningSteps() {

        return jobRepository.getJobNames().stream()
                .flatMap(jobName -> jobRepository.getJobInstances(jobName, 0, Integer.MAX_VALUE).stream())
                .flatMap(jobInstance -> jobRepository.getJobExecutions(jobInstance).stream())
                .filter(JobExecution::isRunning)
                .flatMap(execution -> execution.getStepExecutions().stream())
                .map(step -> step.getStepName() + " - " + step.getStatus())
                .toList();
    }

    @GetMapping("/history")
    public List<BatchJobStatusDto> getJobHistory() {
        return jobRepository.getJobNames().stream()
                .flatMap(jobName -> jobRepository.getJobInstances(jobName, 0, Integer.MAX_VALUE).stream())
                .flatMap(jobInstance -> jobRepository.getJobExecutions(jobInstance).stream())
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .map(this::toDto)
                .toList();
    }

    private BatchJobStatusDto toDto(JobExecution execution) {
        return new BatchJobStatusDto(
                execution.getJobInstance().getJobName(),
                execution.getId(),
                execution.getStatus().name(),
                execution.getStartTime(),
                execution.getEndTime());
    }
}
