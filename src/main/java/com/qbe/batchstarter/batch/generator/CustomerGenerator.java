package com.qbe.batchstarter.batch.generator;

import com.qbe.batchstarter.tasklet.CustomerGenerationTasklet;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CustomerGenerator {

    @Bean
    public Step generateCustomersStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            CustomerGenerationTasklet tasklet) {
        return new StepBuilder("generateCustomersStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }
}
