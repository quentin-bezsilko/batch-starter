package com.qbe.batchstarter.batch.reporting.transaction;

import com.qbe.batchstarter.dto.TransactionReportDto;
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
public class TransactionReportJobConfig {

    @Bean
    public Step transactionReportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ListItemReader<TransactionReportDto> transactionReportReader,
            FlatFileItemWriter<TransactionReportDto> transactionReportWriter) {

        return new StepBuilder("transactionReportStep", jobRepository)
                .<TransactionReportDto, TransactionReportDto>chunk(10)
                .reader(transactionReportReader)
                .writer(transactionReportWriter)
                .transactionManager(transactionManager)
                .build();
    }
}
