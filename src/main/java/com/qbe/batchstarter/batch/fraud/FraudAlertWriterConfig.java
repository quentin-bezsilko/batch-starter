package com.qbe.batchstarter.batch.fraud;

import com.qbe.batchstarter.entity.FraudAlertEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FraudAlertWriterConfig {

    @Bean
    public JpaItemWriter<FraudAlertEntity> fraudAlertWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<FraudAlertEntity> writer = new JpaItemWriter<>(entityManagerFactory);
        writer.setUsePersist(true);
        return writer;
    }
}
