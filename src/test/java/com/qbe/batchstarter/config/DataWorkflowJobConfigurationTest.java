package com.qbe.batchstarter.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;

class DataWorkflowJobConfigurationTest {

    private final DataWorkflowJobConfiguration configuration = new DataWorkflowJobConfiguration();

    @Test
    void shouldCreateDataWorkflowJob() {
        JobRepository jobRepository = mock(JobRepository.class);

        Step generateCustomersStep = mock(Step.class);
        Step generateAccountsStep = mock(Step.class);
        Step generateTransactionsStep = mock(Step.class);
        Step customerImportStep = mock(Step.class);
        Step accountImportStep = mock(Step.class);
        Step transactionImportStep = mock(Step.class);

        Job job = configuration.dataWorkflowJob(
                jobRepository,
                generateCustomersStep,
                generateAccountsStep,
                generateTransactionsStep,
                customerImportStep,
                accountImportStep,
                transactionImportStep);

        assertThat(job).isNotNull();
        assertThat(job.getName()).isEqualTo("dataWorkflowJob");
    }
}
