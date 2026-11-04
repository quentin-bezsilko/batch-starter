package com.qbe.batchstarter.batch.reporting.fraud;

import com.qbe.batchstarter.dto.FraudReportDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class FraudReportWriterConfig {

    private final GeneratorProperties generatorProperties;

    @Bean
    public FlatFileItemWriter<FraudReportDto> fraudReportWriter() {
        return new FlatFileItemWriterBuilder<FraudReportDto>()
                .name("fraudReportWriter")
                .shouldDeleteIfExists(true)
                .resource(new FileSystemResource(
                        generatorProperties.outputDirectory() + generatorProperties.fraudReportFileName()))
                .headerCallback(writer -> writer.write("fraudType,fraudCount,totalAmount"))
                .delimited()
                .delimiter(",")
                .names("fraudType", "fraudCount", "totalAmount")
                .build();
    }
}
