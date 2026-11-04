package com.qbe.batchstarter.batch.reporting.bestclients;

import com.qbe.batchstarter.dto.BestClientReportDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class BestClientReportWriterConfig {

    private final GeneratorProperties generatorProperties;

    @Bean
    public FlatFileItemWriter<BestClientReportDto> bestClientReportWriter() {
        return new FlatFileItemWriterBuilder<BestClientReportDto>()
                .name("bestClientReportWriter")
                .shouldDeleteIfExists(true)
                .resource(new FileSystemResource(
                        generatorProperties.outputDirectory() + generatorProperties.bestClientReportFileName()))
                .headerCallback(writer -> writer.write("customerId,customerName,accountCount,totalBalance"))
                .delimited()
                .delimiter(",")
                .names("customerId", "customerName", "accountCount", "totalBalance")
                .build();
    }
}
