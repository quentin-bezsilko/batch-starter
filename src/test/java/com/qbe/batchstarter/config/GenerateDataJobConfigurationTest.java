package com.qbe.batchstarter.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;

class GenerateDataJobConfigurationTest {

    private final GenerateDataJobConfiguration configuration = new GenerateDataJobConfiguration();

    @Test
    void shouldCreateGenerateDataJob() {
        JobRepository jobRepository = mock(JobRepository.class);

        Step generateCustomersStep = mock(Step.class);
        Step generateAccountsStep = mock(Step.class);
        Step generateTransactionsStep = mock(Step.class);

        Job job = configuration.generateDataJob(
                jobRepository, generateCustomersStep, generateAccountsStep, generateTransactionsStep);

        assertThat(job).isNotNull();
        assertThat(job.getName()).isEqualTo("generateDataJob");
    }
}
