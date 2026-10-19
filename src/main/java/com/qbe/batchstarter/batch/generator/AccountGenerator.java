package com.qbe.batchstarter.batch.generator;

import com.qbe.batchstarter.tasklet.AccountGenerationTasklet;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class AccountGenerator {

    @Bean
    public Step generateAccountsStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            AccountGenerationTasklet tasklet) {
        return new StepBuilder("generateAccountsStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }
}
