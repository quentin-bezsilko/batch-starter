package com.qbe.batchstarter.config;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportDataJobConfiguration {

    @Bean
    public Job importDataJob(
            JobRepository jobRepository,
            @Qualifier("customerImportStep") Step customerImportStep,
            @Qualifier("accountImportStep") Step accountImportStep,
            @Qualifier("transactionImportStep") Step transactionImportStep) {
        return new JobBuilder("importDataJob", jobRepository)
                .start(customerImportStep)
                .next(accountImportStep)
                .next(transactionImportStep)
                .build();
    }
}
