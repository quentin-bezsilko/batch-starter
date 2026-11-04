package com.qbe.batchstarter.batch.fraud;

import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.properties.BatchProperties;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FraudAlertReaderConfig {

    private final BatchProperties batchProperties;

    @Bean
    @StepScope
    public JpaPagingItemReader<BankTransactionEntity> fraudReader(EntityManagerFactory entityManagerFactory) {
        JpaPagingItemReader<BankTransactionEntity> reader = new JpaPagingItemReader<>(entityManagerFactory);
        reader.setName("fraudReader");
        reader.setPageSize(batchProperties.chunkSize());
        reader.setQueryString(
                """
                select t
                from BankTransactionEntity t
                """);
        return reader;
    }
}
