package com.qbe.batchstarter.batch.enrichment;

import com.qbe.batchstarter.dto.CustomerEnrichmentDto;
import com.qbe.batchstarter.entity.CustomerEntity;
import com.qbe.batchstarter.listener.ObservedStepListener;
import com.qbe.batchstarter.properties.BatchProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class CustomerEnrichmentJobConfig {

    private final PlatformTransactionManager transactionManager;
    private final JpaPagingItemReader<CustomerEntity> customerEnrichmentReader;
    private final CustomerEnrichmentProcessor customerProcessor;
    private final FlatFileItemWriter<CustomerEnrichmentDto> customerEnrichmentWriter;
    private final BatchProperties batchProperties;

    @Bean
    public Step customerEnrichmentStep(JobRepository jobRepository, ObservedStepListener listener) {
        return new StepBuilder("customerEnrichmentStep", jobRepository)
                .<CustomerEntity, CustomerEnrichmentDto>chunk(batchProperties.chunkSize())
                .reader(customerEnrichmentReader)
                .processor(customerProcessor)
                .writer(customerEnrichmentWriter)
                .listener(listener)
                .transactionManager(transactionManager)
                .build();
    }
}
