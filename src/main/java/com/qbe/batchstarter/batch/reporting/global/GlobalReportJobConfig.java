package com.qbe.batchstarter.batch.reporting.global;

import com.qbe.batchstarter.dto.GlobalReportDto;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class GlobalReportJobConfig {

    @Bean
    public Step globalReportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ListItemReader<GlobalReportDto> globalReportReader,
            FlatFileItemWriter<GlobalReportDto> globalReportWriter) {

        return new StepBuilder("globalReportStep", jobRepository)
                .<GlobalReportDto, GlobalReportDto>chunk(1)
                .reader(globalReportReader)
                .writer(globalReportWriter)
                .transactionManager(transactionManager)
                .build();
    }
}
