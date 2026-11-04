package com.qbe.batchstarter.batch.enrichment;

import com.qbe.batchstarter.dto.CustomerEnrichmentDto;
import com.qbe.batchstarter.properties.GeneratorProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class CustomerEnrichmentWriterConfig {

    private final GeneratorProperties generatorProperties;

    @Bean
    public FlatFileItemWriter<CustomerEnrichmentDto> customerEnrichmentWriter() {
        return new FlatFileItemWriterBuilder<CustomerEnrichmentDto>()
                .name("customerSummaryWriter")
                .resource(new FileSystemResource(
                        generatorProperties.outputDirectory() + generatorProperties.customerEnrichmentFileName()))
                .headerCallback(writer -> writer.write(
                        "customerId,customerName,accountCount,totalBalance,transactionCount,totalCredit,totalDebit"))
                .delimited()
                .delimiter(",")
                .names(
                        "customerId",
                        "customerName",
                        "accountCount",
                        "totalBalance",
                        "transactionCount",
                        "totalCredit",
                        "totalDebit")
                .build();
    }
}
