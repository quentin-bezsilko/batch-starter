package com.qbe.batchstarter.batch.importer.transaction;

import com.qbe.batchstarter.entity.BankTransactionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionWriterConfig {

    @Bean
    public JpaItemWriter<BankTransactionEntity> transactionWriter(EntityManagerFactory emf) {
        return new JpaItemWriter<>(emf);
    }
}
