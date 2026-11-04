package com.qbe.batchstarter.batch.reporting.global;

import com.qbe.batchstarter.dto.GlobalReportDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class GlobalReportWriterConfig {

    private final GeneratorProperties generatorProperties;

    @Bean
    public FlatFileItemWriter<GlobalReportDto> globalReportWriter() {
        return new FlatFileItemWriterBuilder<GlobalReportDto>()
                .name("globalReportWriter")
                .shouldDeleteIfExists(true)
                .resource(new FileSystemResource(
                        generatorProperties.outputDirectory() + generatorProperties.globalReportFileName()))
                .headerCallback(writer -> writer.write(
                        "reportDate,totalCustomers,totalAccounts,totalTransactions,totalFrauds,totalBalance"))
                .delimited()
                .delimiter(",")
                .names(
                        "reportDate",
                        "totalCustomers",
                        "totalAccounts",
                        "totalTransactions",
                        "totalFrauds",
                        "totalBalance")
                .build();
    }
}
