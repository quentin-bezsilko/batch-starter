package com.qbe.batchstarter.batch.reporting.customer;

import com.qbe.batchstarter.dto.CustomerReportDto;
import com.qbe.batchstarter.dto.CustomerReportExportDto;
import com.qbe.batchstarter.listener.ObservedStepListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CustomerReportJobConfig {

    @Bean
    public Step customerReportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ListItemReader<CustomerReportDto> customerReportReader,
            ItemProcessor<CustomerReportDto, CustomerReportExportDto> customerReportProcessor,
            FlatFileItemWriter<CustomerReportExportDto> customerReportWriter,
            ObservedStepListener listener) {

        return new StepBuilder("customerReportStep", jobRepository)
                .<CustomerReportDto, CustomerReportExportDto>chunk(100)
                .reader(customerReportReader)
                .processor(customerReportProcessor)
                .writer(customerReportWriter)
                .listener(listener)
                .transactionManager(transactionManager)
                .build();
    }
}
