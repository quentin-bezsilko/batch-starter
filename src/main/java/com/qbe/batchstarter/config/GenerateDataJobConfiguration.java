package com.qbe.batchstarter.config;

import com.qbe.batchstarter.tasklet.AccountGenerationTasklet;
import com.qbe.batchstarter.tasklet.CustomerGenerationTasklet;
import com.qbe.batchstarter.tasklet.TransactionGenerationTasklet;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class GenerateDataJobConfiguration {

    @Bean
    public Step generateCustomersStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            CustomerGenerationTasklet tasklet) {

        return new StepBuilder("generateCustomersStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Step generateAccountsStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            AccountGenerationTasklet tasklet) {

        return new StepBuilder("generateAccountsStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Step generateTransactionsStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            TransactionGenerationTasklet tasklet) {

        return new StepBuilder("generateTransactionsStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean
    public Job generateDataJob(
            JobRepository jobRepository,
            Step generateCustomersStep,
            Step generateAccountsStep,
            Step generateTransactionsStep) {

        return new JobBuilder("generateDataJob", jobRepository)
                .start(generateCustomersStep)
                .next(generateAccountsStep)
                .next(generateTransactionsStep)
                .build();
    }
}
