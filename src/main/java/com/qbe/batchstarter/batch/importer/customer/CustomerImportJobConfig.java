package com.qbe.batchstarter.batch.importer.customer;

import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.entity.CustomerEntity;
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
public class CustomerImportJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final FlatFileItemReader<CustomerDto> customerReader;
    private final CustomerProcessorConfig customerProcessor;
    private final JpaItemWriter<CustomerEntity> customerWriter;

    @Bean
    public Step customerImportStep() {
        ChunkOrientedStepBuilder<CustomerDto, CustomerEntity> builder =
                new StepBuilder("customerImportStep", jobRepository).chunk(1000);

        return builder.transactionManager(transactionManager)
                .reader(customerReader)
                .processor(customerProcessor)
                .writer(customerWriter)
                .build();
    }

    @Bean
    public Job customerImportJob(Step customerImportStep) {
        return new JobBuilder("customerImportJob", jobRepository)
                .start(customerImportStep)
                .build();
    }
}
