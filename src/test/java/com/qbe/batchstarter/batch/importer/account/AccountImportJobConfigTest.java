package com.qbe.batchstarter.batch.importer.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.properties.BatchProperties;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.support.SynchronizedItemStreamReader;
import org.springframework.transaction.PlatformTransactionManager;

public class AccountImportJobConfigTest {

    @Test
    void shouldCreateSynchronizedReader() {
        AccountImportJobConfig config = new AccountImportJobConfig(
                mock(PlatformTransactionManager.class),
                mock(FlatFileItemReader.class),
                mock(AccountProcessorConfig.class),
                mock(JpaItemWriter.class),
                mock(BatchProperties.class));

        SynchronizedItemStreamReader<BankAccountDto> reader = config.accountReaderSync();
        assertThat(reader).isNotNull();
    }

    @Test
    void shouldCreateJob() {
        AccountImportJobConfig config = new AccountImportJobConfig(
                mock(PlatformTransactionManager.class),
                mock(FlatFileItemReader.class),
                mock(AccountProcessorConfig.class),
                mock(JpaItemWriter.class),
                mock(BatchProperties.class));

        Job job = config.accountImportJob(mock(JobRepository.class), mock(Step.class));
        assertThat(job.getName()).isEqualTo("accountImportJob");
    }
}
