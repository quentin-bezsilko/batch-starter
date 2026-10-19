package com.qbe.batchstarter.batch.generator;

import com.qbe.batchstarter.tasklet.TransactionGenerationTasklet;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionGenerator {

    @Bean
    public Step generateTransactionsStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            TransactionGenerationTasklet tasklet) {
        return new StepBuilder("generateTransactionsStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }
}
