package com.qbe.batchstarter.batch.importer.transaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.partition.TransactionPartitioner;
import com.qbe.batchstarter.properties.BatchProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

class TransactionImportJobConfigTest {

    private TransactionImportJobConfig config;

    @BeforeEach
    void setUp() {
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        TransactionProcessor transactionProcessor = mock(TransactionProcessor.class);
        JpaItemWriter<BankTransactionEntity> transactionWriter = mock(JpaItemWriter.class);
        BatchProperties batchProperties = mock(BatchProperties.class);

        when(batchProperties.chunkSize()).thenReturn(100);
        when(batchProperties.skipLimit()).thenReturn(10);
        when(batchProperties.partitioningGridSize()).thenReturn(4);

        config = new TransactionImportJobConfig(
                transactionManager, transactionProcessor, transactionWriter, batchProperties);
    }

    @Test
    void shouldInstantiateConfiguration() {
        assertThat(config).isNotNull();
    }

    @Test
    void shouldCreateTransactionImportJob() {
        Job job = config.transactionImportJob(mock(JobRepository.class), mock(Step.class));
        assertThat(job).isNotNull();
        assertThat(job.getName()).isEqualTo("transactionImportJob");
    }

    @Test
    void shouldCreateTransactionImportStep() {
        Step step = config.transactionImportStep(
                mock(JobRepository.class),
                mock(TransactionPartitioner.class),
                mock(AsyncTaskExecutor.class),
                mock(Step.class));

        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("transactionImportStep");
    }

    @Test
    void shouldCreateTransactionWorkerStep() {
        Step step = config.transactionWorkerStep(mock(JobRepository.class), mock(ItemStreamReader.class));
        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("transactionWorkerStep");
    }
}
