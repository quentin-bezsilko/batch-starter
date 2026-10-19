package com.qbe.batchstarter.batch.importer.account;

import com.qbe.batchstarter.entity.BankAccountEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountWriterConfig {

    @Bean
    public JpaItemWriter<BankAccountEntity> accountWriter(EntityManagerFactory emf) {
        return new JpaItemWriter<>(emf);
    }
}
