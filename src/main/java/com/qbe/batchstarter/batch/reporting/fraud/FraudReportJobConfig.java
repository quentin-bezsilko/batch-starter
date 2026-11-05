package com.qbe.batchstarter.batch.reporting.fraud;

import com.qbe.batchstarter.dto.FraudReportDto;
import com.qbe.batchstarter.listener.ObservedStepListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class FraudReportJobConfig {

    @Bean
    public Step fraudReportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ListItemReader<FraudReportDto> fraudReportReader,
            FlatFileItemWriter<FraudReportDto> fraudReportWriter,
            ObservedStepListener listener) {

        return new StepBuilder("fraudReportStep", jobRepository)
                .<FraudReportDto, FraudReportDto>chunk(10)
                .reader(fraudReportReader)
                .writer(fraudReportWriter)
                .listener(listener)
                .transactionManager(transactionManager)
                .build();
    }
}
