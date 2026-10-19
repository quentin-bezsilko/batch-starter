package com.qbe.batchstarter.config;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenerateDataJobConfiguration {

    @Bean
    public Job generateDataJob(
            JobRepository jobRepository,
            @Qualifier("generateCustomersStep") Step generateCustomersStep,
            @Qualifier("generateAccountsStep") Step generateAccountsStep,
            @Qualifier("generateTransactionsStep") Step generateTransactionsStep) {
        return new JobBuilder("generateDataJob", jobRepository)
                .start(generateCustomersStep)
                .next(generateAccountsStep)
                .next(generateTransactionsStep)
                .build();
    }
}
