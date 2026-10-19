package com.qbe.batchstarter.batch.importer.transaction;

import com.qbe.batchstarter.dto.BankTransactionDto;
import com.qbe.batchstarter.entity.BankTransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.ChunkOrientedStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class TransactionImportJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final FlatFileItemReader<BankTransactionDto> transactionReader;
    private final TransactionProcessorConfig transactionProcessor;
    private final JpaItemWriter<BankTransactionEntity> transactionWriter;

    @Bean
    public Step transactionImportStep() {
        ChunkOrientedStepBuilder<BankTransactionDto, BankTransactionEntity> builder =
                new StepBuilder("transactionImportStep", jobRepository).chunk(1000);

        return builder.transactionManager(transactionManager)
                .reader(transactionReader)
                .processor(transactionProcessor)
                .writer(transactionWriter)
                .build();
    }

    @Bean
    public Job transactionImportJob(Step transactionImportStep) {
        return new JobBuilder("transactionImportJob", jobRepository)
                .start(transactionImportStep)
                .build();
    }
}
