package com.qbe.batchstarter.batch.importer.account;

import com.qbe.batchstarter.dto.BankAccountDto;
import com.qbe.batchstarter.entity.BankAccountEntity;
import com.qbe.batchstarter.exception.InvalidBankAccountException;
import com.qbe.batchstarter.exception.TemporaryException;
import com.qbe.batchstarter.properties.BatchProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.ChunkOrientedStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class AccountImportJobConfig {

    private final PlatformTransactionManager transactionManager;
    private final FlatFileItemReader<BankAccountDto> accountReader;
    private final AccountProcessorConfig accountProcessor;
    private final JpaItemWriter<BankAccountEntity> accountWriter;
    private final BatchProperties batchProperties;

    @Bean
    public SynchronizedItemStreamReader<BankAccountDto> accountReaderSync() {
        return new SynchronizedItemStreamReader<>(accountReader);
    }

    @Bean
    public Step accountImportStep(
            JobRepository jobRepository,
            ObjectProvider<AsyncTaskExecutor> taskExecutorProvider,
            SynchronizedItemStreamReader<BankAccountDto> accountReaderSync) {
        ChunkOrientedStepBuilder<BankAccountDto, BankAccountEntity> builder = new StepBuilder(
                        "accountImportStep", jobRepository)
                .<BankAccountDto, BankAccountEntity>chunk(batchProperties.chunkSize())
                .reader(accountReaderSync)
                .processor(accountProcessor)
                .writer(accountWriter)
                .faultTolerant()
                .skip(InvalidBankAccountException.class)
                .skipLimit(100)
                .retry(TemporaryException.class)
                .retryLimit(3);

        taskExecutorProvider.ifAvailable(builder::taskExecutor);

        return builder.transactionManager(transactionManager).build();
    }

    @Bean
    public Job accountImportJob(JobRepository jobRepository, Step accountImportStep) {
        return new JobBuilder("accountImportJob", jobRepository)
                .start(accountImportStep)
                .build();
    }
}
