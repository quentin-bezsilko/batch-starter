package com.qbe.batchstarter.batch.importer.customer;

import com.qbe.batchstarter.dto.CustomerDto;
import com.qbe.batchstarter.entity.CustomerEntity;
import com.qbe.batchstarter.listener.ObservedJobListener;
import com.qbe.batchstarter.listener.ObservedStepListener;
import com.qbe.batchstarter.properties.BatchProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class CustomerImportJobConfig {

    private final PlatformTransactionManager transactionManager;
    private final FlatFileItemReader<CustomerDto> customerReader;
    private final CustomerProcessor customerProcessor;
    private final JpaItemWriter<CustomerEntity> customerWriter;
    private final BatchProperties batchProperties;

    @Bean
    public Step customerImportStep(JobRepository jobRepository, ObservedStepListener listener) {
        return new StepBuilder("customerImportStep", jobRepository)
                .<CustomerDto, CustomerEntity>chunk(batchProperties.chunkSize())
                .reader(customerReader)
                .processor(customerProcessor)
                .writer(customerWriter)
                .listener(listener)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Job customerImportJob(JobRepository jobRepository, Step customerImportStep, ObservedJobListener listener) {
        return new JobBuilder("customerImportJob", jobRepository)
                .listener(listener)
                .start(customerImportStep)
                .build();
    }
}
