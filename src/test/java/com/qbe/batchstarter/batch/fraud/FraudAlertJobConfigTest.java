package com.qbe.batchstarter.batch.fraud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.entity.FraudAlertEntity;
import com.qbe.batchstarter.properties.BatchProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.support.transaction.ResourcelessTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

class FraudAlertJobConfigTest {

    private final FraudAlertJobConfig config = new FraudAlertJobConfig();

    @Test
    void shouldCreateFraudDetectionStep() {
        JobRepository jobRepository = mock(JobRepository.class);
        PlatformTransactionManager transactionManager = new ResourcelessTransactionManager();

        JpaPagingItemReader<BankTransactionEntity> reader = mock(JpaPagingItemReader.class);
        FraudAlertProcessor processor = mock(FraudAlertProcessor.class);
        JpaItemWriter<FraudAlertEntity> writer = mock(JpaItemWriter.class);
        BatchProperties properties = mock(BatchProperties.class);
        when(properties.chunkSize()).thenReturn(100);

        Step step = config.fraudDetectionStep(jobRepository, transactionManager, reader, processor, writer, properties);

        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("fraudDetectionStep");
    }
}
