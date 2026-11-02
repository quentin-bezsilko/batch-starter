package com.qbe.batchstarter.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;

class ImportDataJobConfigurationTest {

    private final ImportDataJobConfiguration configuration = new ImportDataJobConfiguration();

    @Test
    void shouldCreateImportDataJob() {
        JobRepository jobRepository = mock(JobRepository.class);

        Step customerImportStep = mock(Step.class);
        Step accountImportStep = mock(Step.class);
        Step transactionImportStep = mock(Step.class);

        Job job = configuration.importDataJob(
                jobRepository, customerImportStep, accountImportStep, transactionImportStep);
        assertThat(job).isNotNull();
        assertThat(job.getName()).isEqualTo("importDataJob");
    }
}
