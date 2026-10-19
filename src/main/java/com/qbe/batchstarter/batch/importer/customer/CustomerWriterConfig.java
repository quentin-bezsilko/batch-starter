package com.qbe.batchstarter.batch.importer.customer;

import com.qbe.batchstarter.entity.CustomerEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerWriterConfig {

    @Bean
    public JpaItemWriter<CustomerEntity> customerWriter(EntityManagerFactory emf) {
        return new JpaItemWriter<>(emf);
    }
}
