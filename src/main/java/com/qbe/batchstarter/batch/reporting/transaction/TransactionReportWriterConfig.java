package com.qbe.batchstarter.batch.reporting.transaction;

import com.qbe.batchstarter.dto.TransactionReportDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class TransactionReportWriterConfig {

    private final GeneratorProperties generatorProperties;

    @Bean
    public FlatFileItemWriter<TransactionReportDto> transactionReportWriter() {
        return new FlatFileItemWriterBuilder<TransactionReportDto>()
                .name("transactionReportWriter")
                .shouldDeleteIfExists(true)
                .resource(new FileSystemResource(
                        generatorProperties.outputDirectory() + generatorProperties.transactionReportFileName()))
                .headerCallback(writer -> writer.write("transactionType,transactionCount,totalAmount,averageAmount"))
                .delimited()
                .delimiter(",")
                .names("transactionType", "transactionCount", "totalAmount", "averageAmount")
                .build();
    }
}
