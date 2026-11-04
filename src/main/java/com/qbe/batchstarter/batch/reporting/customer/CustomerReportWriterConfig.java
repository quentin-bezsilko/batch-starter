package com.qbe.batchstarter.batch.reporting.customer;

import com.qbe.batchstarter.dto.CustomerReportExportDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class CustomerReportWriterConfig {

    private final GeneratorProperties generatorProperties;

    @Bean
    public FlatFileItemWriter<CustomerReportExportDto> customerReportWriter() {
        return new FlatFileItemWriterBuilder<CustomerReportExportDto>()
                .name("customerReportWriter")
                .shouldDeleteIfExists(true)
                .resource(new FileSystemResource(
                        generatorProperties.outputDirectory() + generatorProperties.customerReportFileName()))
                .headerCallback(writer -> writer.write("customerId,customerName,country,age,accountCount,totalBalance"))
                .delimited()
                .delimiter(",")
                .names("customerId", "customerName", "country", "age", "accountCount", "totalBalance")
                .build();
    }
}
