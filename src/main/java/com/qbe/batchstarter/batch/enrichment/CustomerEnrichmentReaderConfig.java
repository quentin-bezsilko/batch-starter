package com.qbe.batchstarter.batch.enrichment;

import com.qbe.batchstarter.entity.CustomerEntity;
import com.qbe.batchstarter.properties.BatchProperties;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CustomerEnrichmentReaderConfig {

    private final BatchProperties batchProperties;

    @Bean
    @StepScope
    public JpaPagingItemReader<CustomerEntity> customerEnrichmentReader(EntityManagerFactory entityManagerFactory) {
        JpaPagingItemReader<CustomerEntity> reader = new JpaPagingItemReader<>(entityManagerFactory);
        reader.setName("customerReader");
        reader.setQueryString("select c from CustomerEntity c");
        reader.setPageSize(batchProperties.chunkSize());
        return reader;
    }
}
