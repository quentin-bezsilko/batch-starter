package com.qbe.batchstarter.batch.importer.transaction;

import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.exception.InvalidBankTransactionException;
import com.qbe.batchstarter.partition.TransactionPartitioner;
import com.qbe.batchstarter.properties.BatchProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class TransactionImportJobConfig {

    private final PlatformTransactionManager transactionManager;
    private final TransactionProcessorConfig transactionProcessor;
    private final JpaItemWriter<BankTransactionEntity> transactionWriter;
    private final BatchProperties batchProperties;

    @Bean
    public Step transactionImportStep(
            JobRepository jobRepository,
            TransactionPartitioner partitioner,
            AsyncTaskExecutor taskExecutor,
            Step transactionWorkerStep) {
        return new StepBuilder("transactionImportStep", jobRepository)
                .partitioner("transactionWorkerStep", partitioner)
                .step(transactionWorkerStep)
                .gridSize(batchProperties.partitioningGridSize())
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Step transactionWorkerStep(
            JobRepository jobRepository, ItemStreamReader<BankTransactionDto> transactionReader) {
        return new StepBuilder("transactionWorkerStep", jobRepository)
                .<BankTransactionDto, BankTransactionEntity>chunk(batchProperties.chunkSize())
                .reader(transactionReader)
                .processor(transactionProcessor)
                .writer(transactionWriter)
                .faultTolerant()
                .skip(InvalidBankTransactionException.class)
                .skipLimit(batchProperties.skipLimit())
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Job transactionImportJob(JobRepository jobRepository, Step transactionImportStep) {
        return new JobBuilder("transactionImportJob", jobRepository)
                .start(transactionImportStep)
                .build();
    }
}
