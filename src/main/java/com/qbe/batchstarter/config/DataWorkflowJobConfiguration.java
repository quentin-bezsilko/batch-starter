package com.qbe.batchstarter.config;

import com.qbe.batchstarter.listener.ObservedJobListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DataWorkflowJobConfiguration {

    @Bean
    public Job dataWorkflowJob(
            JobRepository jobRepository,
            @Qualifier("generateCustomersStep") Step generateCustomersStep,
            @Qualifier("generateAccountsStep") Step generateAccountsStep,
            @Qualifier("generateTransactionsStep") Step generateTransactionsStep,
            @Qualifier("customerImportStep") Step customerImportStep,
            @Qualifier("accountImportStep") Step accountImportStep,
            @Qualifier("transactionImportStep") Step transactionImportStep,
            @Qualifier("customerEnrichmentStep") Step customerEnrichmentStep,
            @Qualifier("fraudDetectionStep") Step fraudDetectionStep,
            @Qualifier("globalReportStep") Step globalReportStep,
            @Qualifier("customerReportStep") Step customerReportStep,
            @Qualifier("bestClientReportStep") Step bestClientReportStep,
            @Qualifier("fraudReportStep") Step fraudReportStep,
            @Qualifier("transactionReportStep") Step transactionReportStep,
            ObservedJobListener listener) {
        return new JobBuilder("dataWorkflowJob", jobRepository)
                .listener(listener)
                .start(generateCustomersStep)
                .next(generateAccountsStep)
                .next(generateTransactionsStep)
                .next(customerImportStep)
                .next(accountImportStep)
                .next(transactionImportStep)
                .next(customerEnrichmentStep)
                .next(fraudDetectionStep)
                .next(globalReportStep)
                .next(customerReportStep)
                .next(bestClientReportStep)
                .next(fraudReportStep)
                .next(transactionReportStep)
                .build();
    }
}
