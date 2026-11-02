package com.qbe.batchstarter.batch.importer.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.properties.BatchProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.transaction.PlatformTransactionManager;

class TransactionImportJobConfigTest {

    @Test
    void shouldCreateTransactionImportJob() {
        TransactionImportJobConfig config = new TransactionImportJobConfig(
                mock(PlatformTransactionManager.class),
                mock(TransactionProcessorConfig.class),
                mock(JpaItemWriter.class),
                mock(BatchProperties.class));

        Job job = config.transactionImportJob(mock(JobRepository.class), mock(Step.class));

        assertThat(job).isNotNull();
        assertThat(job.getName()).isEqualTo("transactionImportJob");
    }

    @Test
    void shouldInstantiateConfiguration() {
        TransactionImportJobConfig config = new TransactionImportJobConfig(
                mock(PlatformTransactionManager.class),
                mock(TransactionProcessorConfig.class),
                mock(JpaItemWriter.class),
                mock(BatchProperties.class));

        assertThat(config).isNotNull();
    }
}
