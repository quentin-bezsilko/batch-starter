package com.qbe.batchstarter.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.listener.ObservedJobListener;
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
        Step customerEnrichmentStep = mock(Step.class);
        Step fraudDetectionStep = mock(Step.class);
        Step globalReportStep = mock(Step.class);
        Step customerReportStep = mock(Step.class);
        Step bestClientReportStep = mock(Step.class);
        Step transactionReportStep = mock(Step.class);
        Step fraudReportStep = mock(Step.class);
        ObservedJobListener observedJobListener = mock(ObservedJobListener.class);

        Job job = configuration.dataWorkflowJob(
                jobRepository,
                generateCustomersStep,
                generateAccountsStep,
                generateTransactionsStep,
                customerImportStep,
                accountImportStep,
                transactionImportStep,
                customerEnrichmentStep,
                fraudDetectionStep,
                globalReportStep,
                customerReportStep,
                bestClientReportStep,
                fraudReportStep,
                transactionReportStep,
                observedJobListener);

        assertThat(job).isNotNull();
        assertThat(job.getName()).isEqualTo("dataWorkflowJob");
    }
}
