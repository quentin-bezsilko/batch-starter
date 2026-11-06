package com.qbe.batchstarter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BatchAutoStartConfiguration {

    @Bean
    ApplicationRunner batchRunner(JobOperator jobOperator, Job dataWorkflowJob) {

        return args -> {
            log.info("Starting demo batch...");

            JobExecution execution = jobOperator.start(
                    dataWorkflowJob,
                    new JobParametersBuilder()
                            .addLong("run.id", System.currentTimeMillis())
                            .toJobParameters());

            log.info("Batch started successfully. executionId={}", execution.getId());
        };
    }
}
