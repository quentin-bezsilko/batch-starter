package com.qbe.batchstarter.batch.reporting.bestclients;

import com.qbe.batchstarter.dto.BestClientReportDto;
import com.qbe.batchstarter.listener.ObservedStepListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BestClientReportJobConfig {

    @Bean
    public Step bestClientReportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ListItemReader<BestClientReportDto> bestClientReportReader,
            FlatFileItemWriter<BestClientReportDto> bestClientReportWriter,
            ObservedStepListener listener) {

        return new StepBuilder("bestClientReportStep", jobRepository)
                .<BestClientReportDto, BestClientReportDto>chunk(20)
                .reader(bestClientReportReader)
                .writer(bestClientReportWriter)
                .listener(listener)
                .transactionManager(transactionManager)
                .build();
    }
}
