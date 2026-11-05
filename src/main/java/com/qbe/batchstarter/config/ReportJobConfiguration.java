package com.qbe.batchstarter.config;

import com.qbe.batchstarter.listener.ObservedJobListener;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportJobConfiguration {

    @Bean
    public Job reportJob(
            JobRepository jobRepository,
            @Qualifier("globalReportStep") Step globalReportStep,
            @Qualifier("customerReportStep") Step customerReportStep,
            @Qualifier("bestClientReportStep") Step bestClientReportStep,
            @Qualifier("fraudReportStep") Step fraudReportStep,
            @Qualifier("transactionReportStep") Step transactionReportStep,
            ObservedJobListener listener) {
        return new JobBuilder("reportJob", jobRepository)
                .listener(listener)
                .start(globalReportStep)
                .next(customerReportStep)
                .next(bestClientReportStep)
                .next(fraudReportStep)
                .next(transactionReportStep)
                .build();
    }
}
