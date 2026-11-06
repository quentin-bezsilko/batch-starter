package com.qbe.batchstarter.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.listener.ObservedJobListener;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;

class ReportJobConfigurationTest {

    private final ReportJobConfiguration config = new ReportJobConfiguration();

    @Test
    void shouldCreateReportJob() {
        JobRepository jobRepository = mock(JobRepository.class);

        Step globalReportStep = mock(Step.class);
        Step customerReportStep = mock(Step.class);
        Step bestClientReportStep = mock(Step.class);
        Step fraudReportStep = mock(Step.class);
        Step transactionReportStep = mock(Step.class);

        ObservedJobListener listener = mock(ObservedJobListener.class);

        Job job = config.reportJob(
                jobRepository,
                globalReportStep,
                customerReportStep,
                bestClientReportStep,
                fraudReportStep,
                transactionReportStep,
                listener);

        assertThat(job).isNotNull();
        assertThat(job.getName()).isEqualTo("reportJob");
    }
}
