package com.qbe.batchstarter.batch.fraud;

import com.qbe.batchstarter.entity.BankTransactionEntity;
import com.qbe.batchstarter.entity.FraudAlertEntity;
import com.qbe.batchstarter.properties.BatchProperties;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FraudAlertJobConfig {

    @Bean
    public Step fraudDetectionStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            JpaPagingItemReader<BankTransactionEntity> fraudReader,
            FraudAlertProcessor fraudProcessor,
            JpaItemWriter<FraudAlertEntity> fraudAlertWriter,
            BatchProperties properties) {

        return new StepBuilder("fraudDetectionStep", jobRepository)
                .<BankTransactionEntity, FraudAlertEntity>chunk(properties.chunkSize())
                .reader(fraudReader)
                .processor(fraudProcessor)
                .writer(fraudAlertWriter)
                .transactionManager(transactionManager)
                .build();
    }
}
