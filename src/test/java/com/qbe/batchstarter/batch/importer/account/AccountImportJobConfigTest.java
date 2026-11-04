package com.qbe.batchstarter.batch.importer.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.properties.BatchProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

class AccountImportJobConfigTest {

    private AccountImportJobConfig config;

    @BeforeEach
    void setUp() {
        PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        FlatFileItemReader<BankAccountDto> accountReader = mock(FlatFileItemReader.class);
        AccountProcessor accountProcessor = mock(AccountProcessor.class);
        JpaItemWriter<BankAccountEntity> accountWriter = mock(JpaItemWriter.class);
        BatchProperties batchProperties = mock(BatchProperties.class);

        when(batchProperties.chunkSize()).thenReturn(100);

        config = new AccountImportJobConfig(
                transactionManager, accountReader, accountProcessor, accountWriter, batchProperties);
    }

    @Test
    void shouldInstantiateConfiguration() {
        assertThat(config).isNotNull();
    }

    @Test
    void shouldCreateSynchronizedReader() {
        SynchronizedItemStreamReader<BankAccountDto> reader = config.accountReaderSync();
        assertThat(reader).isNotNull();
    }

    @Test
    void shouldCreateAccountImportStep() {
        @SuppressWarnings("unchecked")
        ObjectProvider<AsyncTaskExecutor> provider = mock(ObjectProvider.class);

        Step step =
                config.accountImportStep(mock(JobRepository.class), provider, mock(SynchronizedItemStreamReader.class));
        assertThat(step).isNotNull();
        assertThat(step.getName()).isEqualTo("accountImportStep");
    }

    @Test
    void shouldCreateJob() {
        Job job = config.accountImportJob(mock(JobRepository.class), mock(Step.class));
        assertThat(job).isNotNull();
        assertThat(job.getName()).isEqualTo("accountImportJob");
    }
}
