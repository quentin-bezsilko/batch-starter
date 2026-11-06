package com.qbe.batchstarter.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

class BatchAutoStartConfigurationTest {

    private final BatchAutoStartConfiguration configuration = new BatchAutoStartConfiguration();

    @Test
    void shouldStartBatchOnApplicationStartup() throws Exception {
        JobOperator jobOperator = Mockito.mock(JobOperator.class);
        Job job = Mockito.mock(Job.class);
        JobExecution execution = Mockito.mock(JobExecution.class);

        when(execution.getId()).thenReturn(1L);
        when(jobOperator.start(any(Job.class), any(JobParameters.class))).thenReturn(execution);

        ApplicationRunner runner = configuration.batchRunner(jobOperator, job);
        runner.run(Mockito.mock(ApplicationArguments.class));

        verify(jobOperator).start(any(Job.class), any(JobParameters.class));
    }
}
